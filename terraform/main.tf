terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# VPC
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name = "soulapp-vpc"
  }
}

# Subnets
resource "aws_subnet" "public" {
  count             = 2
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.${count.index + 1}.0/24"
  availability_zone = data.aws_availability_zones.available.names[count.index]

  tags = {
    Name = "soulapp-public-subnet-${count.index + 1}"
  }
}

# Internet Gateway
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "soulapp-igw"
  }
}

# Route Table
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = {
    Name = "soulapp-public-rt"
  }
}

# Route Table Associations
resource "aws_route_table_association" "public" {
  count          = 2
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

# Security Group
resource "aws_security_group" "app" {
  name        = "soulapp-sg"
  description = "Security group for The Soul App"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "soulapp-sg"
  }
}

# Elastic Beanstalk Application
resource "aws_elastic_beanstalk_application" "app" {
  name        = "soulapp-backend"
  description = "The Soul App Backend"
}

# Elastic Beanstalk Environment
resource "aws_elastic_beanstalk_environment" "app" {
  name                = "soulapp-backend-env"
  application         = aws_elastic_beanstalk_application.app.name
  solution_stack_name = "64bit Amazon Linux 2023 v4.0.1 running Corretto 21"

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = "t3.micro"
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MinSize"
    value     = "1"
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = "4"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "EnvironmentType"
    value     = "LoadBalanced"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "LoadBalancerType"
    value     = "application"
  }
}

# RDS Instance
resource "aws_db_instance" "app" {
  identifier           = "soulapp-db"
  engine              = "mysql"
  engine_version      = "8.0"
  instance_class      = "db.t3.micro"
  allocated_storage   = 20
  storage_type        = "gp2"
  db_name             = "soulapp"
  username            = var.db_username
  password            = var.db_password
  skip_final_snapshot = true

  vpc_security_group_ids = [aws_security_group.app.id]
  db_subnet_group_name   = aws_db_subnet_group.app.name

  tags = {
    Name = "soulapp-db"
  }
}

# RDS Subnet Group
resource "aws_db_subnet_group" "app" {
  name       = "soulapp-db-subnet-group"
  subnet_ids = aws_subnet.public[*].id

  tags = {
    Name = "soulapp-db-subnet-group"
  }
}

# S3 Bucket
resource "aws_s3_bucket" "app" {
  bucket = "soulapp-deployments"
  acl    = "private"

  versioning {
    enabled = true
  }

  tags = {
    Name = "soulapp-deployments"
  }
}

# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "app" {
  name              = "/aws/elasticbeanstalk/soulapp-backend-env"
  retention_in_days = 30

  tags = {
    Name = "soulapp-logs"
  }
}

# Outputs
output "elastic_beanstalk_url" {
  value = aws_elastic_beanstalk_environment.app.cname
}

output "rds_endpoint" {
  value = aws_db_instance.app.endpoint
}

output "s3_bucket_name" {
  value = aws_s3_bucket.app.id
} 
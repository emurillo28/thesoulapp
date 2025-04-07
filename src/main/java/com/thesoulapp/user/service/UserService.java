import com.thesoulapp.subscription.service.SubscriptionService;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Get subscription information
        var subscription = subscriptionService.getUserSubscription(email);

        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .subscriptionTier(subscription.getTier().name())
                .points(0) // Placeholder until we implement gamification
                .build();
    }

    // ... existing methods ...
} 
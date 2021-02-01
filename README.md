# Hilt Install Binding Extension

Reduces a bit of boilerplate for single implementation bindings.
Example usage:
```kotlin
@InstallBinding(SingletonComponent::class)
class AuthenticatorImpl @Inject constructor(): Authenticator
```

## Disclaimer
This is **not** an official Google product.
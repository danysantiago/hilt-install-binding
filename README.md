# Hilt Install Binding Extension

A Hilt extension that reduces a bit of boilerplate for single implementation bindings.
Example usage:
```kotlin
@InstallBinding(SingletonComponent::class)
class AuthenticatorImpl @Inject constructor(): Authenticator
```
the above example is equivalent to:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface BindAuthModule {
  @Binds
  fun bind(impl: AuthenticatorImpl): Authenticator
}
```

## Disclaimer
This is **not** an official Google product.
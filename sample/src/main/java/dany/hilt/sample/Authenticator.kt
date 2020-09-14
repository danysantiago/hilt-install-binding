package dany.hilt.sample

import dagger.hilt.components.SingletonComponent
import dany.hilt.InstallBinding
import javax.inject.Inject

interface Authenticator

@InstallBinding(component = SingletonComponent::class)
class AuthenticatorImpl @Inject constructor() : Authenticator
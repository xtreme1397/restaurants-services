package org.xtreme.com.user.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.xtreme.com.user.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUsernameAndStatus(String username, String status);

	User findByUsername(String username);

	@Query(value = "{'refreshTokens.tokenId' : ?0 }", fields = "{ 'refreshTokens.tokenId' : 1 }")
	User findByRefreshTokensTokenID(String tokenID);

}

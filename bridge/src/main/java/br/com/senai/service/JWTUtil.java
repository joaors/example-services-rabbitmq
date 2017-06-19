package br.com.senai.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTSigner.Options;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

public class JWTUtil {

	private static final String SECRET = "ZyO2LZarRXS1LIXhcx18-123rDwcfvfFserbs35-564fvnRTDwqqrBGTIY297";
	private static final int COUNTPREFIX = "Bearer".length()+1;
	private static final int TEMPO_VIDA_TOKEN = 18000; /* segundos - 5 horas*/
	public static final String HEADER = "Authorization";

	/**
	 * 
	 * @param username
	 *            Custom param for the middle part of the token.
	 * @return The token string.
	 */
	public static String createToken(String username) {
		JWTSigner signer = new JWTSigner(SECRET);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("user", username);
		String token = signer.sign(claims, getOption());
		return token;

	}
		
	private static Options getOption() {
		return new JWTSigner
						.Options()
						.setAlgorithm(Algorithm.HS256)
						.setExpirySeconds(TEMPO_VIDA_TOKEN)
						.setIssuedAt(true);
	}
	
	public static String createTokenMap(Map<String, Object> claims) {
		JWTSigner signer = new JWTSigner(SECRET);
		String token = signer.sign(claims, getOption());
		return token;
	}	

	public static Map<String, Object> decode(String token) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, SignatureException, IOException, JWTVerifyException {
		String auth = token.substring(COUNTPREFIX);		
		JWTVerifier verifier = new JWTVerifier(SECRET);
		Map<String, Object> map = null;
		map = verifier.verify(auth);
		return map;
	}	


}

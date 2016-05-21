
package com.dangdang.unit.keeper.verification;

public interface IVerifier {
	public void valid(String expJsonText, String actJsonText) throws Exception;
}

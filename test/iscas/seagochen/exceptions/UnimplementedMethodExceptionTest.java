package iscas.seagochen.exceptions;

import org.junit.Test;

public class UnimplementedMethodExceptionTest {

	@Test
	public void exceptionTest(){
		try {
			throw new UnimplementedMethodException("sssss");
		} catch (UnimplementedMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

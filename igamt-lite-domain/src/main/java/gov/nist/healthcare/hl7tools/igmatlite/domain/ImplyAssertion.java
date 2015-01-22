package gov.nist.healthcare.hl7tools.igmatlite.domain;

public class ImplyAssertion extends Assertion{
	private Assertion firstChildAssertion;
	private Assertion SecondChildAssertion;
	public ImplyAssertion() {
		super();
	}
	public Assertion getFirstChildAssertion() {
		return firstChildAssertion;
	}
	public void setFirstChildAssertion(Assertion firstChildAssertion) {
		this.firstChildAssertion = firstChildAssertion;
	}
	public Assertion getSecondChildAssertion() {
		return SecondChildAssertion;
	}
	public void setSecondChildAssertion(Assertion secondChildAssertion) {
		SecondChildAssertion = secondChildAssertion;
	}


	
	
	
}
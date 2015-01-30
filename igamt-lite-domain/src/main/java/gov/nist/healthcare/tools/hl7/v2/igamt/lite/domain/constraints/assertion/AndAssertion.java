package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints.assertion;

public class AndAssertion extends Assertion{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6573748270496061078L;
	private Assertion firstChildAssertion;
	private Assertion SecondChildAssertion;
	public AndAssertion() {
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
	@Override
	public String toString() {
		return "AndAssertion [firstChildAssertion=" + firstChildAssertion
				+ ", SecondChildAssertion=" + SecondChildAssertion + "]";
	}
	
	
}
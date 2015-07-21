package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ValidateEmailRequest {
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("email", email).toString();
	}
}

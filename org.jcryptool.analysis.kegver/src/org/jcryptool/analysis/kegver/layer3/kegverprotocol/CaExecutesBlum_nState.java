package org.jcryptool.analysis.kegver.layer3.kegverprotocol;

import org.jcryptool.analysis.kegver.layer3.U;

public class CaExecutesBlum_nState extends KegverStateSuper implements KegverStateBehavior {

	public CaExecutesBlum_nState(KegverStateContext inKegVer) {
		super(inKegVer);
	}

	public void setup() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void bothUnigen_r() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caAbortsUnigen_r() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userAbortsUnigen_r() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void bothUnigen_s() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caAbortsUnigen_s() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userAbortsUnigen_s() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userGenerates_p() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userAborts_p() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userGenerates_q() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userAborts_q() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userSends_Cp() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caVerifiesPOK_Cp() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caAborts_Cp() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userSends_Cq() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caVerifiesPOK_Cq() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caAborts_Cq() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userSends_n() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caVerifiesPOK_n() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void caAborts_Cn() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void userExecutesBlum_n() {
		U.verbose(new Throwable(), "Wrong state");		

	}

	public void caExecutesBlum_n() {

		// Report
		U.verbose(new Throwable(), "entered");
		
		// Execute this state
		boolean isBlum_n = this.getKegver().getCA().executeBlum_n();
		
		// Report
		U.verbose(new Throwable(), 
				"CA: " + this.getKegver().getCA().toString_() +
				", User: " + this.getKegver().getUser().toString_() +
				", KegverData: " + this.getKegver().getKegverData() +
				", Commitment_Cp: " + this.getKegver().getKegverData().getCommitment_Cp() +
				", Commitment_Cq: " + this.getKegver().getKegverData().getCommitment_Cq() +
				", N: " + this.getKegver().getKegverData().get_N() + 
				", isBlum_n: " + isBlum_n);
		U.verbose(new Throwable(), "Assume isBlum_n true");
		isBlum_n = true;

		// Trigger next state
		if(isBlum_n){
			this.getKegver().setState(KegverStateContext.getBothAreHappy());
			this.getKegver().bothAreHappy();
		} else {
			this.getKegver().setState(KegverStateContext.getCaAbortsBlum_n());
			this.getKegver().caAbortsBlum_n();
		}

	}

	public void caAbortsBlum_n() {
		U.verbose(new Throwable(), "Wrong state");		
	}

	public void bothAreHappy() {
		U.verbose(new Throwable(), "Wrong state");		
	}
}
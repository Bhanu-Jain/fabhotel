package com.fabhotel.Eras.applicationlayer;

import com.fabhotel.Eras.model.User;

public class FormattedEndorsement {
	private String formattedEndorsement;

    public FormattedEndorsement(String endorsedBy, int actualScore, double adjustedScore, String reason) {
        this.formattedEndorsement = String.format("%s - %d (%.1f with %s)", endorsedBy, actualScore, adjustedScore, reason);
    }

    public FormattedEndorsement(User reviewee, double adjustedScore, String comment) {
		//TODO Auto-generated constructor stub
	}

	public String getFormattedEndorsement() {
        return formattedEndorsement;
    }

    public void setFormattedEndorsement(String formattedEndorsement) {
        this.formattedEndorsement = formattedEndorsement;
    }

}

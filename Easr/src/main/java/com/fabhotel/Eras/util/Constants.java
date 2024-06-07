package com.fabhotel.Eras.util;

public class Constants {
	public static final double MAX_EXPERIENCE_WEIGHT = 1.0;
    public static final double CO_WORKER_WEIGHT = 1.2;
    public static final double EXPERIENCE_DIVISOR = 10.0;

    public static final String ERROR_REVIEWEE_NOT_FOUND = "Reviewee not found";
    public static final String ERROR_REVIEWER_NOT_FOUND = "Reviewer not found";
    public static final String ERROR_SKILL_NOT_FOUND = "Skill not found";
    public static final int SAME_FIELD_BONUS=15;
    public static final int COWORKER_BONUS=10;
	public static final double BASE_SCORE = 50.0;
	public static final double EXPERIENCE_BONUS = 10;
	public static final int EXACT_SKILL_MATCH=10;
	public static final int BRODER_SKILL_MATCH=5;
	public static final int SUB_SKILL_SAME_BRODER_MATCH=3;
	public static final int SKILL_MISMATCH=1;
	public static final Object ADDFORSKILLTEXT = " An additional score is added for their shared skill proficiency.";
	public static final Object ADDFORCOWORKTEXT = "users are coworkers from the same company, an additional bonus is applied";
	public static final Object NOTACOWORKER = "Not a coworker, no bonus applied";
	public static final Object SAMEFIELDBONUSTEXT = "Additional score is awarded due to their shared professional field";
	public static final Object ADDEXPBONUSTEXT = " An extra score is granted due to one Reviewer greater experience in the shared professional field.";
	public static final Object NOTFORSAMEFIELDTEXT = "No bonus as field is not same";

}

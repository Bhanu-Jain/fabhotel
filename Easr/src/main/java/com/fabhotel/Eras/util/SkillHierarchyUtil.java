package com.fabhotel.Eras.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SkillHierarchyUtil {
	public static final Map<TechSkill, Set<TechSkill>> skillHierarchy = new HashMap<>();
    public static final Map<TechSkill, Set<TechSkill>> reverseSkillHierarchy = new HashMap<>();

    static {
        // Define broader skill relationships
        Set<TechSkill> softwareDevSkills = new HashSet<>();
        softwareDevSkills.add(TechSkill.BACKEND_DEVELOPMENT);
        skillHierarchy.put(TechSkill.SOFTWARE_DEVELOPMENT, softwareDevSkills);

        Set<TechSkill> backendDevSkills = new HashSet<>();
        backendDevSkills.add(TechSkill.JAVA);
        backendDevSkills.add(TechSkill.SPRING_BOOT);
        backendDevSkills.add(TechSkill.NODEJS);
        skillHierarchy.put(TechSkill.BACKEND_DEVELOPMENT, backendDevSkills);

        Set<TechSkill> javaSkills = new HashSet<>();
        javaSkills.add(TechSkill.SPRING_BOOT);
        skillHierarchy.put(TechSkill.JAVA, javaSkills);

        Set<TechSkill> jsSkills = new HashSet<>();
        jsSkills.add(TechSkill.NODEJS);
        skillHierarchy.put(TechSkill.JS, jsSkills);

        // Define reverse relationships for broader skill checking
        addReverseRelationship(TechSkill.SOFTWARE_DEVELOPMENT, TechSkill.BACKEND_DEVELOPMENT);
        addReverseRelationship(TechSkill.BACKEND_DEVELOPMENT, TechSkill.JAVA);
        addReverseRelationship(TechSkill.BACKEND_DEVELOPMENT, TechSkill.SPRING_BOOT);
        addReverseRelationship(TechSkill.BACKEND_DEVELOPMENT, TechSkill.NODEJS);
        addReverseRelationship(TechSkill.JAVA, TechSkill.SPRING_BOOT);
        addReverseRelationship(TechSkill.JS, TechSkill.NODEJS);
    }
    private static void addReverseRelationship(TechSkill broader, TechSkill subSkill) {
        reverseSkillHierarchy.computeIfAbsent(subSkill, k -> new HashSet<>()).add(broader);
    }
    public static boolean isBroaderSkill(TechSkill endorserSkill, TechSkill endorsedSkill) {
        // Traverse the hierarchy to check if the broaderSkill is indeed a broader skill of the subSkill
        Set<TechSkill> visited = new HashSet<>();
        return isBroaderSkillRecursive(endorserSkill, endorsedSkill, visited);
    }
    private static boolean isBroaderSkillRecursive(TechSkill currentSkill, TechSkill endorsedSkill, Set<TechSkill> visited) {
        if (skillHierarchy.containsKey(currentSkill)) {
            Set<TechSkill> subSkills = skillHierarchy.get(currentSkill);
            if (subSkills.contains(endorsedSkill)) {
                return true;
            }
            // Avoid circular references
            visited.add(currentSkill);
            for (TechSkill subSkill : subSkills) {
                if (!visited.contains(subSkill) && isBroaderSkillRecursive(subSkill, endorsedSkill, visited)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean isBroaderSkillReverseRecursive(TechSkill currentSkill, TechSkill targetSkill, Set<TechSkill> visited) {
        if (reverseSkillHierarchy.containsKey(currentSkill)) {
            Set<TechSkill> broaderSkills = reverseSkillHierarchy.get(currentSkill);
            if (broaderSkills.contains(targetSkill)) {
                return true;
            }
            // Avoid circular references
            visited.add(currentSkill);
            for (TechSkill broaderSkill : broaderSkills) {
                if (!visited.contains(broaderSkill) && isBroaderSkillReverseRecursive(broaderSkill, targetSkill, visited)) {
                    return true;
                }
            }
        }
        return false;
    }
}

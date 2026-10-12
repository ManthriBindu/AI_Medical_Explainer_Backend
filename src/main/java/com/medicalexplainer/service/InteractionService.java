package com.medicalexplainer.service;

import com.medicalexplainer.dto.InteractionRequest;
import com.medicalexplainer.dto.InteractionResponse;
import com.medicalexplainer.dto.InteractionResponse.InteractionItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InteractionService {

    private static final String INTERACTION_DISCLAIMER =
            "Important: This drug interaction check reviews well-known common medication pairings for general educational awareness. " +
            "It does not include every possible clinical combination, food-drug interaction, or individual allergy. " +
            "Never stop or modify your prescription medications without direct consultation with your prescribing doctor or licensed pharmacist.";

    private static final List<KnownInteractionRule> KNOWN_RULES = new ArrayList<>();

    static {
        addRule("aspirin", "ibuprofen", "Moderate",
                "Both medicines belong to the anti-inflammatory (NSAID) family. Taking them together may increase stomach sensitivity or irritation.",
                "Discuss with your doctor or pharmacist. They may suggest separating the doses or substituting a gentle pain reliever.");

        addRule("aspirin", "warfarin", "High",
                "Both medications thin the blood. Combining them significantly increases the risk of bleeding or bruising.",
                "Contact your prescribing physician immediately to review blood clotting checks and personalized dosage guidance.");

        addRule("metformin", "alcohol", "Moderate",
                "Combining Metformin with regular or heavy alcohol intake can lower blood sugar unpredictably and increase metabolic stress.",
                "Limit alcohol consumption and ask your physician about safe beverage guidelines with meals.");

        addRule("lisinopril", "potassium", "High",
                "Lisinopril helps relax blood vessels but can retain potassium. Adding potassium supplements can lead to excessively high potassium levels in the blood.",
                "Your doctor should review routine electrolyte blood panels before combining potassium supplements.");

        addRule("lisinopril", "spironolactone", "High",
                "Combining an ACE inhibitor with a potassium-sparing diuretic can cause elevated potassium levels (hyperkalemia).",
                "Discuss with your cardiologist or nephrologist for regular kidney and electrolyte monitoring.");

        addRule("atorvastatin", "clarithromycin", "High",
                "Certain macrolide antibiotics can temporarily raise statin levels in the bloodstream, increasing the likelihood of muscle soreness.",
                "Consult your doctor. A common approach is temporarily pausing the statin during the short antibiotic course.");

        addRule("simvastatin", "clarithromycin", "High",
                "Macrolide antibiotics significantly increase simvastatin concentration, elevating the potential for muscle discomfort.",
                "Inform your doctor or pharmacist prior to beginning the antibiotic course.");

        addRule("levothyroxine", "calcium", "Moderate",
                "Calcium supplements can bind to thyroid medicine in the stomach, reducing how much medicine your body absorbs.",
                "Take your thyroid tablet in the morning on an empty stomach and space calcium supplements at least 4 hours apart.");

        addRule("levothyroxine", "iron", "Moderate",
                "Iron supplements reduce the absorption and effectiveness of thyroid hormone medication.",
                "Allow at least 4 hours between taking your thyroid medication and your iron supplement.");

        addRule("paracetamol", "acetaminophen", "Moderate",
                "Paracetamol and Acetaminophen are the same active medicine. Taking multiple multi-symptom cold/flu products together risks exceeding safe daily limits.",
                "Check product ingredient lists to ensure total combined daily intake stays strictly under 4,000 mg.");

        addRule("omeprazole", "clopidogrel", "Moderate",
                "Omeprazole may reduce the activation of the blood-thinner clopidogrel (Plavix).",
                "Consult your doctor about alternative acid-reducing medications such as pantoprazole.");
    }

    private static void addRule(String drugA, String drugB, String severity, String description, String recommendation) {
        KNOWN_RULES.add(new KnownInteractionRule(drugA.toLowerCase(), drugB.toLowerCase(), severity, description, recommendation));
    }

    public InteractionResponse checkInteractions(InteractionRequest request) {
        List<String> rawNames = request.getMedicineNames();
        InteractionResponse response = new InteractionResponse();
        response.setDisclaimer(INTERACTION_DISCLAIMER);

        if (rawNames == null || rawNames.size() < 2) {
            response.setInteractionFound(false);
            response.setTotalInteractions(0);
            response.setGeneralSummary("Please enter or select at least 2 medications to check for potential interactions.");
            return response;
        }

        List<String> cleanNames = rawNames.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .toList();

        List<InteractionItem> found = new ArrayList<>();
        Set<String> pairedKeys = new HashSet<>();

        for (int i = 0; i < cleanNames.size(); i++) {
            for (int j = i + 1; j < cleanNames.size(); j++) {
                String medA = cleanNames.get(i);
                String medB = cleanNames.get(j);

                for (KnownInteractionRule rule : KNOWN_RULES) {
                    boolean matchForward = medA.contains(rule.drugA) && medB.contains(rule.drugB);
                    boolean matchReverse = medA.contains(rule.drugB) && medB.contains(rule.drugA);

                    if (matchForward || matchReverse) {
                        String key = rule.drugA + "_" + rule.drugB;
                        if (!pairedKeys.contains(key)) {
                            pairedKeys.add(key);
                            found.add(new InteractionItem(
                                    capitalize(medA),
                                    capitalize(medB),
                                    rule.severity,
                                    rule.description,
                                    rule.recommendation
                            ));
                        }
                    }
                }
            }
        }

        if (found.isEmpty()) {
            response.setInteractionFound(false);
            response.setTotalInteractions(0);
            response.setGeneralSummary("No common interactions were found between the medications you entered. " +
                    "Always confirm new combinations with your doctor or pharmacist.");
        } else {
            response.setInteractionFound(true);
            response.setTotalInteractions(found.size());
            response.setInteractions(found);
            response.setGeneralSummary("We identified " + found.size() + " potential medication interaction" +
                    (found.size() > 1 ? "s" : "") + " to discuss with your healthcare professional.");
        }

        return response;
    }

    private String capitalize(String text) {
        if (text == null || text.isBlank()) return text;
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    private record KnownInteractionRule(String drugA, String drugB, String severity, String description, String recommendation) {}
}

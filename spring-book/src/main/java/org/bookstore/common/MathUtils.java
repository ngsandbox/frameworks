package org.bookstore.common;

public class MathUtils {

    public static double cosineSimilarity(char[] vectorA, char[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        int length = Math.min(vectorA.length, vectorB.length);
        for (int i = 0; i < length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}

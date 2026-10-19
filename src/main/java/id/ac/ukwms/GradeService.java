package id.ac.ukwms;

/**
 * Class sederhana untuk pengolahan nilai mahasiswa.
 * Berisi 3 method dengan logika bersyarat:
 *  1. isPassing()          -> validasi kelulusan
 *  2. classify()           -> klasifikasi nilai huruf
 *  3. calculateFinalScore() -> perhitungan nilai akhir berbobot
 */
public class GradeService {

    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 100;
    public static final int PASSING_SCORE = 60;

    /** Lulus jika nilai >= 60. Nilai harus berada pada rentang 0-100. */
    public boolean isPassing(int score) {
        validateScore(score);
        return score >= PASSING_SCORE;
    }

    /**
     * Klasifikasi nilai huruf:
     * 85-100 = A, 70-84 = B, 60-69 = C, 50-59 = D, 0-49 = E
     */
    public char classify(int score) {
        validateScore(score);
        if (score >= 85) return 'A';
        if (score >= 70) return 'B';
        if (score >= 60) return 'C';
        if (score >= 50) return 'D';
        return 'E';
    }

    /**
     * Nilai akhir = 30% tugas + 30% UTS + 40% UAS.
     * Jika nilai UAS di bawah 40, nilai akhir dibatasi maksimal 59 (tidak lulus).
     */
    public double calculateFinalScore(int tugas, int uts, int uas) {
        validateScore(tugas);
        validateScore(uts);
        validateScore(uas);

        double akhir = (tugas * 0.30) + (uts * 0.30) + (uas * 0.40);
        if (uas < 40 && akhir > 59) {
            return 59;
        }
        return akhir;
    }

    private void validateScore(int score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("Nilai harus antara 0 dan 100: " + score);
        }
    }
}

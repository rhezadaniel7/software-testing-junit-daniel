package id.ac.ukwms;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Unit Test GradeService")
class GradeServiceTest {

    private GradeService service;

    // Arrange: objek baru sebelum setiap test (prinsip Independent pada FIRST)
    @BeforeEach
    void setUp() {
        service = new GradeService();
    }

    // ---------- isPassing ----------

    @ParameterizedTest(name = "nilai {0} -> lulus = {1}")
    @DisplayName("isPassing: Boundary Value Analysis pada batas kelulusan 60 dan rentang 0-100")
    @CsvSource({
            "0,   false", // batas bawah rentang
            "1,   false", // tepat di atas batas bawah
            "59,  false", // tepat di bawah batas lulus
            "60,  true", // tepat di batas lulus
            "61,  true", // tepat di atas batas lulus
            "99,  true", // tepat di bawah batas atas
            "100, true" // batas atas rentang
    })
    void isPassing_bva(int score, boolean expected) {
        assertEquals(expected, service.isPassing(score));
    }

    @Test
    @DisplayName("isPassing: nilai 70 dinyatakan lulus")
    void isPassing_nilaiNormalLulus() {
        assertTrue(service.isPassing(70));
    }

    @Test
    @DisplayName("isPassing: nilai 30 dinyatakan tidak lulus")
    void isPassing_nilaiNormalTidakLulus() {
        assertFalse(service.isPassing(30));
    }

    // ---------- classify ----------

    @Test
    @DisplayName("classify: nilai 85 (batas bawah) mendapat A")
    void classify_batasBawahA() {
        assertEquals('A', service.classify(85));
    }

    @Test
    @DisplayName("classify: nilai 84 (tepat di bawah batas A) mendapat B")
    void classify_tepatDiBawahBatasA() {
        assertEquals('B', service.classify(84));
    }

    @Test
    @DisplayName("classify: semua kelas nilai huruf terklasifikasi dengan benar")
    void classify_semuaKelas() {
        assertAll("klasifikasi nilai huruf",
                () -> assertEquals('A', service.classify(100)),
                () -> assertEquals('B', service.classify(70)),
                () -> assertEquals('C', service.classify(60)),
                () -> assertEquals('D', service.classify(50)),
                () -> assertEquals('E', service.classify(49)),
                () -> assertEquals('E', service.classify(0)));
    }

    // ---------- calculateFinalScore ----------

    @Test
    @DisplayName("calculateFinalScore: perhitungan berbobot 30/30/40 benar")
    void calculateFinalScore_hitunganBerbobot() {
        // 80*0.3 + 70*0.3 + 90*0.4 = 24 + 21 + 36 = 81
        assertEquals(81.0, service.calculateFinalScore(80, 70, 90), 0.001);
    }

    @Test
    @DisplayName("calculateFinalScore: UAS < 40 membatasi nilai akhir maksimal 59")
    void calculateFinalScore_uasRendahDibatasi() {
        // 100*0.3 + 100*0.3 + 30*0.4 = 72, tetapi UAS < 40 -> dibatasi 59
        assertEquals(59.0, service.calculateFinalScore(100, 100, 30), 0.001);
    }

    @Test
    @DisplayName("calculateFinalScore: UAS tepat 40 tidak dibatasi")
    void calculateFinalScore_uasTepat40TidakDibatasi() {
        // 100*0.3 + 100*0.3 + 40*0.4 = 76
        assertEquals(76.0, service.calculateFinalScore(100, 100, 40), 0.001);
    }

    // ---------- skenario negatif (assertThrows) ----------

    @Test
    @DisplayName("isPassing: nilai negatif melempar IllegalArgumentException")
    void isPassing_nilaiNegatifDitolak() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> service.isPassing(-1));
        assertEquals("Nilai harus antara 0 dan 100: -1", ex.getMessage());
    }

    @Test
    @DisplayName("classify: nilai di atas 100 melempar IllegalArgumentException")
    void classify_nilaiDiAtas100Ditolak() {
        assertThrows(IllegalArgumentException.class, () -> service.classify(101));
    }

    @Test
    @DisplayName("calculateFinalScore: salah satu komponen tidak valid melempar exception")
    void calculateFinalScore_komponenTidakValidDitolak() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculateFinalScore(80, -5, 90));
    }
}

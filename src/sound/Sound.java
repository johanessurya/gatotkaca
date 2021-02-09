package sound;

/**
 * Class <code>Sound</code> adalah tempat sampel suara disimpan 
 * dalam bentuk byte array
 */
public class Sound {

    private byte[] samples;

    /**
     * Membuat object <code>Sound</code> baru dengan object byte array yang
     * spesifik
     * @param samples sample sound dalam bentuk byte array
     */
    public Sound(byte[] samples) {
        this.samples = samples;
    }

    /**
     * Mengambil byte array object sound ini
     * @return byte array sound
     */
    public byte[] getSamples() {
        return samples;
    }
}

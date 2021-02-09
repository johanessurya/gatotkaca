package util;

/**
 * Class <code>SimpleMath</code> berisi function static yang berguna untuk game ini
 * @author Yohanes Surya
 */
public class SimpleMath {
	/**
	 * Mengcheck sekaligus mengganti jika <code>n</code> melebihi batas yang 
	 * ditentukan. Misal: min=0; max=10; dan n diisi 100 maka akan diisi dengan max
	 * yaitu 10
	 * @param min nilai minimum
	 * @param n nilai yang akan di check
	 * @param max nilai maksimum
	 * @return nilai setelah di check
	 */
	public static float minMax(float min, float n, float max){
    	n=Math.min(n,max);
    	n=Math.max(min,n);    		
		return n;
	}
	
	/**
	 * Mengcheck sekaligus mengganti jika <code>n</code> melebihi batas yang 
	 * ditentukan. Misal: min=0; max=10; dan n diisi 100 maka akan diisi dengan max
	 * yaitu 10
	 * @param min nilai minimum
	 * @param n nilai yang akan di check
	 * @param max nilai maksimum
	 * @return nilai setelah di check
	 */
    public static int minMax(int min, int n, int max){
    	n=Math.min(n,max);
    	n=Math.max(min,n);    		
    	
    	return n;
    }
}

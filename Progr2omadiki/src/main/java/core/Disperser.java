import java.io.*;
import java.util.Random;

public class Disperser {

	public static byte[]
        aTorques = {97, 98, 99, 100, 101, 102};

	public static long mSplother(String fileName) {

		String path = "C:\\Users\\User\\Desktop\\Jarpeb\\" + fileName;
		File file = new File(path);
		long size = file.length();
		size = 1266;
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open file " + fileName);
		}
		long byteSum = 0;
		try {
			for (int i = 0; i < size; i++) {
				int random = new Random().nextInt(aTorques.length);
				out.write(aTorques[random]);
				if (i < 700) {
					byteSum += aTorques[random];
				}
			}
			out.close();
		} catch (Exception e) {
			System.err.println("Unable to open file " + fileName);
		}
		return byteSum;
	}

    public static int[] mSelena(String fileName2) {
    	try (BufferedInputStream in = new BufferedInputStream(
    		new FileInputStream(fileName2))) {
    		int[] a = new int[7];
    		int count = 0;
    		int b;
    		while ((b = in.read()) != -1) {
    			count++;
    			switch (b) {
    				case 97:
    					a[1]++;
    					break;
    				case 98:
				       	a[2]++;
				       	break;
					case 99:
				      	a[3]++;
				        break;
					case 100:
				       	a[4]++;
				        break;
					case 101:
				       	a[5]++;
				        break;
					case 102:
				       	a[6]++;
                        break;
                }
            }
            a[0] = count;
            return a;
        } catch (FileNotFoundException e) {
			System.err.println("Unable to open file " + fileName2
				+ ": " + e.getMessage());
		} catch (IOException e) {
		    System.err.println("Error reading byte: " + e.getMessage());
        }
        int[] b = new int[1];
        return b;
    }
}












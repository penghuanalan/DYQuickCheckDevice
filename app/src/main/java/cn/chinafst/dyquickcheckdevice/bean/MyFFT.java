package cn.chinafst.dyquickcheckdevice.bean;


public class MyFFT {
    public MyFFT() {
    }

    public static void fft(int n,double[] x, double[] y, int ifft) {
        int j;
        int k;
        int jk;
        double[] sin = new double[n];
        double[] cos = new double[n];
        for (int i = 0; i < n; i++) {
            cos[i] = Math.cos(2* Math.PI*i/n);
            sin[i] = Math.sin(2* Math.PI*i/n);
        }
        if(ifft==-1)
        {
            for (int i = 0; i < n; i++) {
                y[i] = -y[i];
            }
        }
        double[] a = new double[n];
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
          a[i]=0;
          b[i]=0;
        }
        for (j = 0; j < n; j++) {
            for (k = 0; k < n; k++) {
                jk = (j * k) % (n);
                a[j] = a[j] + x[k] * cos[jk] - y[k] * sin[jk];
                b[j] = b[j] + y[k] * cos[jk] + x[k] * sin[jk];
            }
        }
     //   LogUtils.d(a);
        if (ifft == -1) {
            for (j = 0; j < n; j++) {
                x[j] = a[j] / n;
                y[j] = -b[j] / n;
            }
        } else {
            for (j = 0; j < n; j++) {
                x[j] = a[j];
                y[j] = b[j];
            }
        }
    }
}

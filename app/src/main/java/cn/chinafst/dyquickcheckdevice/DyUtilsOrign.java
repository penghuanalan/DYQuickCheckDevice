package cn.chinafst.dyquickcheckdevice;


import java.util.ArrayList;

public class DyUtilsOrign {

	/*获取一段数据的波谷信息
	 * origin 处理的原始数组
	 * dValue 差值
	 * point 点数
	 * return : 该段波的信息二维数组
	 * */
	public static int[][] getWaveInfo(double[] origin,double dValue,int point) {
		//定义比较器, (i+1) -i>0 为正
		//起始点
		int[] startPoint= new int[point+1];
		//结束点
		int[] endPoint= new int[point+1];
		//波谷
		int[] wave= new int[point*2];
		//数组长度
		int len =origin.length;
		//所有波谷坐标集合
		ArrayList<Integer> boguPoint= new ArrayList<Integer>();

		//比较器赋值
		for(int i=0;i<point+1;i++){
			if(i==0){
				startPoint[i]=1;
			}else{
				startPoint[i]=-1;
			}

			if(i==point){
				endPoint[i]=-1;
			}else{
				endPoint[i]=1;
			}
		}
		//波谷比较器赋值
		for(int i=0;i<wave.length;i++){
			if(i<wave.length/2){
				wave[i]=-1;
			}else{
				wave[i]=1;
			}
		}
		//定义一个原数据走势
		int[] origin1 = new int[len];
		for(int i=1;i<len;i++){
			if(origin[i]-origin[i-1]>dValue){
				origin1[i]=1;
			}else if(origin[i-1]-origin[i]>dValue){
				origin1[i]=-1;
			}else{
				origin1[i]=0;
			}
		}

		//找波谷
		for(int i=point;i<len-point;i++){
			int[] temp =new int[point*2];
			for(int j =0;j<point*2;j++){
				temp[j]=origin1[j+i-point];
			}
			int arrSum = getArrSum(temp, wave);
			if(arrSum>=point*2-1){
				boguPoint.add(i);
				i=i+point;
			}
		}

		int[][] Sindex=new int[boguPoint.size()][3];


		//找出波谷起始点
		for(int i=0;i<boguPoint.size();i++){
			int s=0;
			if(boguPoint.get(i)-15<0){
				s=0;
			}else{
				s=boguPoint.get(i)-15;
			}
			Sindex[i][1]=boguPoint.get(i);
			for(int j=boguPoint.get(i)-point;j>s;j--){
				int[] chage =new int [point+1];
				for(int m=0;m<point+1;m++){
					chage[m]=origin1[m+j];
				}
				int arrSum = getArrSum(chage,startPoint);
				if(arrSum>=point){
					Sindex[i][0]=j;
					break;
				}
			}



			if(Sindex[i][0]==0){

				if(boguPoint.get(i)-15<0){
					Sindex[i][0]=0;
				}else{
					Sindex[i][0]=boguPoint.get(i)-15;
				}
			}
		}
		//找出波谷结束点
		for(int i=0;i<boguPoint.size();i++){
			//如果长度超过就指定为最后一个
			int s=0;
			if(boguPoint.get(i)+15>len){
				s=len;
			}else{
				s=boguPoint.get(i)+15;
			}

			for(int j=boguPoint.get(i);j<s;j++){
				int[] chage =new int [point+1];
				for(int m=0;m<point+1;m++){
					chage[m]=origin1[j+m-point];
				}
				int arrSum = getArrSum(chage,endPoint);
				if(arrSum>=point){
					Sindex[i][2]=j;
					break;
				}
			}

			if(Sindex[i][2]==0){
				if(boguPoint.get(i)+15>=len){
					Sindex[i][2]=len-1;
				}else{
					Sindex[i][2]=boguPoint.get(i)+15;
				}
			}
		}


		return Sindex;
	}




	//数组乘积和
	private static int getArrSum(int[] origin, int[] compare) {
		// TODO Auto-generated method stub
		if(origin.length!=compare.length){
			System.out.println("--------------------数据错误");

		}
		double sum=0;
		for(int i=0;i<origin.length;i++){
			if(origin[i]==compare[i]){
				sum=sum+1;
			} else if(origin[i]==0){
				//sum=0.91+sum;
				//ph0118小费修改
				sum += (double)(origin.length-2)/origin.length;
			}else{
				sum=sum-1;
			}

			//sum=sum+origin[i]*compare[i];
		}
		//System.out.println("内"+sum);

		return (int) Math.floor(sum);
		//return (int) sum;
	}


	//传入参数 : usefulTemp 起始结束区间赋0的数组 ,m 指定多项式
	public static double[] duoXiangShi(double[] usefulTemp,int m) {

		double[][] a = new double[m][m];
		double[] b = new double[m];
		double[] c = new double[m];
		for(int i=0;i<usefulTemp.length;i++){
			if(usefulTemp[i]!=0){
				for(int j=0;j<m;j++){
					for(int k=0;k<m;k++){
						if(j+k==0){
							a[j][k]=a[j][k]+1;
						}else{
							if(i==0){
								a[j][k]=a[j][k];
							}else{
								a[j][k]=a[j][k]+Math.pow(i*0.1, j+k);
							}

						}
					}
					if(j==0){
						b[j]=b[j]+usefulTemp[i];
					}else{
						if(i==0){
							b[j]=b[j];
						}else{
							b[j]=b[j]+usefulTemp[i]*Math.pow(i*0.1, j);
						}


					}


				}
			}
		}
		//a的逆矩阵


		a=getMatrix(a);
		for(int i=0;i<m;i++){
			for(int j=0;j<m;j++){
				c[i]=c[i]+a[j][i]*b[j];
			}
		}



		double[] temp3 = new double[usefulTemp.length];
		for(int i=0;i<usefulTemp.length;i++){
			for(int j=0;j<m;j++){
				temp3[i]=temp3[i]+c[j]*Math.pow(0.1*i, j);
			}
		}
		return temp3;
	}

	public static double[][] getMatrix(double[][] arr) {
		double abs=getJuZhengMo(arr);
		if(abs==0){
			System.out.println("数据错误");
			return null;
		}

		double[][] changeFunction = getChangeFunction(arr);
		changeFunction=	getYuZiXizang(changeFunction);
		int cols=changeFunction.length;
		int rows=changeFunction[0].length;

		for(int i= 0;i<cols;i++){
			for(int j=0;j<rows;j++){
				if((i+j)%2==0){
					changeFunction[i][j]=changeFunction[i][j]/abs;
				}else{
					changeFunction[i][j]=-changeFunction[i][j]/abs;
				}

			}
		}
		return changeFunction;
	}

	//获取矩阵转置
	private static double[][]  getChangeFunction(double[][] arr) {
		// TODO Auto-generated method stub

		int length=arr[0].length;
		double[][] temp= new double[length][length];
		//r为行数
		for(int r=0;r<length;r++){
			//c为列数
			for(int c=0;c<length;c++){
				temp[c][r]=arr[r][c];
			}
		}
		return temp ;
	}
	//求矩阵的模
	private static double getJuZhengMo(double[][] arr) {
		// TODO Auto-generated method stub
		double v=0,val=0;
		int length = arr[0].length;
		if(length==1){
			return arr[0][0];
		}
		int ans =0;
		double[][] B= new double[length -1][length -1];
		for(int i=0;i<length;i++){
			for(int j=0;j<length-1;j++){
				for(int k=0;k<length-1;k++){
					ans=(k<i?k:k+1);
					B[j][k]=arr[j+1][ans];
				}
			}

			v=getJuZhengMo(B);
			if(i%2==0){
				val=val+arr[0][i]*v;
			}else{
				val=val-arr[0][i]*v;
			}
		}
		return val;
	}
	public static double[][] getYuZiXizang(double[][] arr) {

		int cols=arr.length;
		int rows=arr[0].length;
		double[][] temp = new double[cols][rows];

		for(int i=0;i<cols;i++){
			for(int j=0;j<rows;j++){
				double[][] temp1 = new double[cols-1][rows-1];
				for(int c = 0;c < cols-1;c++){
					int c1 = c<i?c:c+1;
					for(int r = 0;r < rows-1;r++){
						int r1 = r<j?r:r+1;
						temp1[c][r] = arr[c1][r1];
					}
				}
				temp[i][j] = getJuZhengMo(temp1);
			}
		}
		return temp;
	}

	//获取有用数据
	public static double[] getUserFulData(double[] arr) {
		//通过定义的开始/结束点(34,205) 前后10个点 找出 最小值
		/*int dStart=34,dEnd=205,space=15,defultEndSpace=60,defultStartSpace=50;


		if(arr.length<dEnd){
			System.out.println("数据长度不够");
			return null;
		}
		double frontIndexValue= arr[dStart-10];
		double backIndexValue= arr[dEnd-10];
		int startIndex=dStart;

		for(int i=dStart;i<=40;i++){
			if(frontIndexValue>arr[i]){
				frontIndexValue=arr[i];
				startIndex=i;
			}
		}

		for(int i=dEnd+10;i>dEnd-10;i--){
			if(backIndexValue>arr[i]){
				backIndexValue=arr[i];
			}
		}

		//根据最小点找出有用波
		int startX=0,endX=0;
		for(int i=44;i<arr.length/2;i++){
			if(arr[i]<frontIndexValue){
				startX=i;
				break;
			}
		}
		for(int i=195;i>arr.length/2;i--){
			if(arr[i]<backIndexValue){
				endX=i;
				break;
			}
		}*/
		double[] useful;
		try {

			useful= new double[arr.length-110];
			System.arraycopy(arr, 50, useful, 0,arr.length-110);

		}catch (Exception e) {
			// TODO: handle exception
			useful= new double[200];
		}


		return useful;
	}

	/*返回两个波 波峰到起始点/结束点 斜率对应值的差值
	 * 参数: first 有效波
	 *  second 趋势线
	 * */

	public static	ArrayList<Double> doubles= new ArrayList<Double>();



	public static int[] index =new int[2];;

	public static double[] getPointValue(double[] first,double[] duoxiangshi) {

		System.out.println("getPointValue"+first.length);
		double[] result=new double[first.length];
		for(int i=0;i<first.length;i++){
			result[i]=first[i]/duoxiangshi[i];
			doubles.add(Double.valueOf(result[i]));
		}

		/*StringBuffer buffer2= new StringBuffer();
		for(int i=0;i<result.length;i++){
			buffer2.append(result[i]+",");
		}
		System.out.println("最终"+buffer2.toString());*/


		//int[][] waveInfo = getWaveInfo(result,0.0005,6);


		//ph0122
		int[][] waveInfo = getWaveInfo(result,0.0005,4);



	/*	StringBuffer buffer4=new StringBuffer();
		for(int i=0;i<waveInfo.length;i++){
			buffer4.append(waveInfo[i][0]+"-"+waveInfo[i][1]+"-"+waveInfo[i][2]);
			buffer4.append("-----------");
		}
		System.out.println("最终波谷"+buffer4.toString());*/
		double[] value= new double[2];
		for(int i=0;i<waveInfo.length;i++){



			int start =waveInfo[i][0];
			int center =waveInfo[i][1];
			int end =waveInfo[i][2];
			//System.out.println("波谷信息"+start+"--"+center+"--"+end);

			//double pinjun =(result[end]-result[start])*(center-start)/(end-start)+result[start];
			double pinjun =(result[end]*(center-start)+result[start]*(end-center))/(end-start)-result[center];
			pinjun = Math.log(1+pinjun)/Math.log(2);


			//Math.log 是以e为底
			if(waveInfo[i][1]<result.length/2){
				if(value[0]<pinjun){
					value[0]=pinjun;
					index[0]=center;
				}
			}else{

				//System.out.println("value[1] "+value[1]+"pinjun "+pinjun+"center "+center +"end "+end);
				if(value[1]<pinjun&&center-index[0]>10&&center-index[0]<35){
					value[1]=pinjun;
					index[1]=center;
				}
			}
		}

		return value;
	}
	public static double[]  dyMath(double[] orign) {
				index[0]=0;
				index[1]=0;
		//平滑处理
		double[] db4wdt = DB4WDT(orign);
		double[] dbflt =DBFLT(db4wdt);
		double[] db4wdt2 =DB4WDT(dbflt);
		double[] dbflt2 = DBFLT(db4wdt2);
		double[] td = new double[dbflt2.length * 2];
		for(int i=0;i<dbflt2.length;i++) {
			td[i]=dbflt2[i];
		}

		double[] db4iwdt =DB4IWDT(td);

		double[] td1 = new double[db4iwdt.length * 2];
		for(int i=0;i<db4iwdt.length;i++) {
			td1[i]=db4iwdt[i];
		}
		double[] arr =DB4IWDT(td1);
		//平滑处理

		double[] newArray=DyUtilsOrign.getUserFulData(arr);

		int[][] bogu = DyUtilsOrign.getWaveInfo(newArray, 1, 4);
		for(int k=0;k<bogu.length;k++) {
			System.out.print("bogu"+bogu[k]+"");
		}

		ArrayList<int[][]> list = new ArrayList<>();

		try {

			for(int i=0;i<bogu.length;i++) {
				if((newArray[bogu[i][0]]-newArray[bogu[i][1]])/(bogu[i][1]-bogu[i][0])>4&&(newArray[bogu[i][2]]-newArray[bogu[i][1]])/(bogu[i][2]-bogu[i][1])>4) {
					int[][] tem= new int[1][3];
					tem[0][0]=bogu[i][0];
					tem[0][1]=bogu[i][1];
					tem[0][2]=bogu[i][2];

					//System.out.println("----------------开始"+tem[0][0]+"--结束"+tem[0][2]);
					list.add(tem);
				}
			}


		}catch (Exception e) {
			// TODO: handle exception
		}


		int[][] bogu2= new int[list.size()][3];
		for(int i=0;i<list.size();i++) {
			bogu2[i][0]=list.get(i)[0][0];
			bogu2[i][1]=list.get(i)[0][1];
			bogu2[i][2]=list.get(i)[0][2];
		}

		double[] temp2= new double[newArray.length];
		for(int i=0;i<newArray.length;i++){
			temp2[i]=newArray[i];
		}

		//波谷值取0
		for(int i=0;i<bogu2.length;i++){
			if(bogu2[i][0]!=0&&bogu2[i][2]!=temp2.length-1) {
				for(int j=bogu2[i][0];j<=bogu2[i][2];j++){
					temp2[j]=0;
				}
			}

		}

		double[] duoXiangShi = DyUtilsOrign.duoXiangShi(temp2,5);
		/*StringBuffer buffer3= new StringBuffer();
		for(int i=0;i<duoXiangShi.length;i++){
			buffer3.append(duoXiangShi[i]+",");
		}
		System.out.println("多项式"+buffer3.toString());*/

		double[] pointValue = DyUtilsOrign.getPointValue(newArray, duoXiangShi);

		return pointValue;
	}

	/*
	 * 小波分解
	 * */
	public static  double[] DB4WDT(double[] pBuf) {

		double[] h = { 0.230378, 0.714847, 0.630881, -0.027984, -0.187035, 0.030841, 0.032883, -0.010597 };
		double[] g = { -0.010597, -0.032883, 0.030841, 0.187035, -0.027984, -0.630881, 0.714847, -0.230378 };
		double[] tmp;
		int i, j, n, pDLen, half, nLen = pBuf.length;

		if (pBuf == null) return null;	//指针空返回NULL
		if (nLen < 8) return null;    	//数据长度小于8返回NULL

		if (nLen % 2 == 1)
		{
			tmp = new double[nLen + 7]; //malloc(sizeof(double) * (nLen + 7));
			pDLen = (nLen + 7);
		}
		else
		{
			tmp = new double[nLen + 6]; //malloc(sizeof(double) * (nLen + 6));
			pDLen = (nLen + 6);
		}

		half = pDLen / 2;
		for (i = 0; i < half; i++)
		{
			/*tmp数组初始化*/
			tmp[i] = 0;
			tmp[i + half] = 0;

			for (j = 0; j < 8; j++)
			{
				n = 2 * i + j - 6;

				if (n >= nLen) n = 2 * nLen - 1 - n;
				else if (n < 0) n = -1 * n - 1;
				tmp[i] += h[j] * pBuf[n];
				tmp[i + half] += g[j] * pBuf[n];
			}
		}
		return tmp;
	}

	/*
	 * 重构
	 * */

	public static double[] DB4IWDT(double[] pBuf) {


		double[] h1 = { -0.010597, 0.032883, 0.030841, -0.187035, -0.027984, 0.630881, 0.714847, 0.230378 };
		//g1[k] = (-1)^k*h1[(1-k)]
		double[] g1 = { -0.230378, 0.714847, -0.630881, -0.027984, 0.187035, 0.030841, -0.032883, -0.010597 };
		double[] tmp;
		int i, j, n, half, nLen = pBuf.length;
		if (pBuf == null) return null;
		if (nLen < 8) return null;
		half = nLen / 2;
		tmp = new double[nLen - 6];//malloc(sizeof(double)*(nLen-6));
		for (i = 0; i < nLen - 6; i++)
		{
			tmp[i] = 0;
			for (j = 0; j < 8; j++)
			{
				n = i + j - 1;
				if (n >= nLen) n = 2 * nLen - 1 - n;
				if (n % 2 == 0)
				{
					tmp[i] += h1[j] * pBuf[n / 2];
				}
				else
				{
					tmp[i] += g1[j] * pBuf[half + n / 2];
				}
			}
		}
		return tmp;
	}
	public static double[] DBFLT(double[] db){
		double[] rtn = new double[db.length / 2];
		for(int i=0;i<rtn.length;i++) {
			rtn[i]=db[i];
		}
		return rtn;
	}
}

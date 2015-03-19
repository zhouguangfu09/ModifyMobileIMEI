## MTK平台手机串号（IMEI）修改APP
####机型修改：
其实跟安卓系统有关的一些信息大多在/system/build.prop这个文件中，尤其是跟机型有关的信息。下面是ZTE U960S3这台手机调试输出的信息：

	![image](https://github.com/zhouguangfu09/ModifyMobileIMEI/blob/master/MTK_IMEI_App/png/1.png)
	
	可以看到型号(model)，商标(brand)，设备(device)，名字（name）等等。修改这些其实是修改build.prop文件。
####IMEI文件存放位置：
*  MP0B_001*的系列文件需要存放在SD卡根目录的imei文件夹中，文件名可以变（不是MP0B_001*），但是不能有其他不包含imei信息的文件，否者替换无效，copy文件到imei文件夹的时候请留意是否有无效文件。

*  包含机型信息的文件需要放置SD卡根目录，文件名为model.txt 。此文件的信息每一行含有两个部分：手机品牌+型号，比如：lenovo A750.中间有一个空格，每一行只有手机品牌和型号之间有空格 多出来的空格无效，多出空格活菏泽无空格会报异常。请输入信息的时候注意是否按要求录入。

	![image](https://github.com/zhouguangfu09/ModifyMobileIMEI/blob/master/MTK_IMEI_App/png/2.png)



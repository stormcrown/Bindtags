# Bindtags

## 简介
  * 在上一家公司的工作成果。去除了一些重要的业务相关代码及和请求参数。
## 源文件结构介绍：
1. 主要代码在com.blozi.bindtags 下
2. com.jar 下是一些手持枪的镭射API，为了兼容各种手持枪，重写了部分类。其他com.zxing为二维码扫描包。
3. account,账号服务功能。实际未启用。
4. activities活动包，activities.fragment,主界面上的几个界面
5. adapter各种适配器。
6. application ,应用层，主要用于存储一些经常变动的数据。如当前门店，当前活动界面等。方便调用。
7. asyncTask 后台任务类。主要是发送请求等，有线上和桌面两个文件夹。
8. model实体类。
9. reciver 广播接收,扫描广播及下载完成广播
10. security 安全，用于加密，未启用
11. service 服务组件，未启用
12. sqlite 数据库工具，未启用
13. util 各种工具类，重要的有
	* BLOZIPreferenceManager ,存储用户各种重要数据，账号，密码等等。
	* DownloadHelper 下载APP工具
	* LoadingDialog 加载界面生成工具。
	* ScanUtil 扫描工具，将所有支持的扫描枪的工具都整合到一起了。
	* SystemMathod 常用方法，方便各活动调用写到一个类里面。
14. view，一些自定义的组件。

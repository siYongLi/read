package net.ninemm.base.plugin.plugins;

import com.jfinal.plugin.IPlugin;
import net.ninemm.base.SystemOptions;
import net.ninemm.base.common.Consts;

public class QiniuPlugin implements IPlugin {

	public QiniuPlugin() {

	}
	
	@Override
	public boolean start() {
		QiniuConfig config = new QiniuConfig(SystemOptions.get(Consts.QINIU_AK), SystemOptions.get(Consts.QINIU_SK));
		QiniuKit.init(config);
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

	public static void refresh(){
		QiniuPlugin qiniuPlugin = new QiniuPlugin();
		qiniuPlugin.stop();
		qiniuPlugin.start();
	}

}

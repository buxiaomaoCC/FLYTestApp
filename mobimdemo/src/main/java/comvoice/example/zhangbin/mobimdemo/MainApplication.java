package comvoice.example.zhangbin.mobimdemo;

import com.mob.MobApplication;
import com.mob.imsdk.MobIM;

import comvoice.example.zhangbin.mobimdemo.model.MsgReceiverListener;

public class MainApplication extends MobApplication {
	private SimpleMobIMMessageReceiver mobMsgRever = null;

	public void onCreate() {
		super.onCreate();
	}

	public void regMsgRev() {
		if (mobMsgRever == null) {
			mobMsgRever = new SimpleMobIMMessageReceiver(this);
			MobIM.addMessageReceiver(mobMsgRever);
		}
	}

	public void onTerminate() {
		super.onTerminate();
		if (mobMsgRever != null) {
			MobIM.removeMessageReceiver(mobMsgRever);
		}
		mobMsgRever = null;
	}

	public void addMsgRever(MsgReceiverListener listener) {
		mobMsgRever.addMsgRever(listener);
	}

	public void removeMsgRever(MsgReceiverListener listener) {
		mobMsgRever.removeMsgRever(listener);
	}

	public void addGroupMsgRever(MsgReceiverListener listener) {
		mobMsgRever.addGroupMsgRever(listener);
	}

	public void removeGroupMsgRever(MsgReceiverListener listener) {
		mobMsgRever.removeGroupMsgRever(listener);
	}

}

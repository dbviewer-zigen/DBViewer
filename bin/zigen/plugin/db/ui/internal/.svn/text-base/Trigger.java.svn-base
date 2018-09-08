package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.rule.TriggerInfo;
import zigen.plugin.db.core.rule.SourceInfo;

public class Trigger extends Source {
	private static final long serialVersionUID = 1L;

	ITable table;

	TriggerInfo triggerInfo;

	public Trigger(ITable table, TriggerInfo triggerInfo){
		super(triggerInfo.getName());
		this.table = table;
		this.triggerInfo = triggerInfo;
		SourceInfo info = new SourceInfo();
		info.setName(triggerInfo.getName());
		info.setOwner(triggerInfo.getOwner());
		info.setType("TRIGGER");
		setSourceInfo(info);

	}
	public ITable getTable(){
		return table;
	}

	public String getOwner(){
		if(triggerInfo == null){
			return null;
		}else{
			return triggerInfo.getOwner();	// trigger's owner
		}
	}
	public String getType(){
		if(triggerInfo == null){
			return null;
		}else{
			return triggerInfo.getType();
		}

	}

	public String getEvent(){
		if(triggerInfo == null){
			return null;
		}else{
			return triggerInfo.getEvent();
		}
	}

	public String getContent(){
		if(triggerInfo == null){
			return null;
		}else{
			return triggerInfo.getContent();
		}
	}
}

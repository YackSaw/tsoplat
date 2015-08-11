package com.futaba.toshiplatoon;

import javax.jdo.annotations.PersistenceCapable;
import java.util.Date;
import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RoomData {
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long id;
     
    
    private int stat;
    
    public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

	@Persistent	
    String rule;			//regular:レギュラー			ranked_tag ranked_private
	
	@Persistent	
    String privateRule;		//<option value="private_rule_regular">ナワバリ</option>
							//<option value="private_rule_ranked_area">ガチエリア</option>
							//<option value="private_rule_ranked_tower">ガチヤグラ</option>
							//<option value="private_rule_ranked_rainmaker">ガチホコ</option>
    
	@Persistent	
    String privateStage;	//<option value="private_stage_decline">デカライン高架下</option>
							//	<option value="private_stage_hakofugu">ハコフグ倉庫</option>
							//	<option value="private_stage_shionome">シオノメ油田</option>
							//	<option value="private_stage_arowana">アロワナモール</option>
							//	<option value="private_stage_bbus">Bバスパーク</option>
							//	<option value="private_stage_mongara">モンガラキャンプ場</option>
							//	<option value="private_stage_hokke">ホッケふ頭</option>
							//	<option value="private_stage_mozuku">モズク農園</option>
							//	<option value="private_stage_tachiuo">タチウオパーキング</option>
							//	<option value="private_stage_negitoro">ネギトロ炭鉱</option>
							//	<option value="private_stage_omakase">おまかせ</option>
							//	<option value="private_stage_others">その他</option>

	@Persistent	
    String members;			//<option value="two">2人</option>
							//	<option value="three">3人</option>
							//	<option value="four">4人</option>
	
	public String getPrivateRule() {
		return privateRule;
	}

	public void setPrivateRule(String privateRule) {
		this.privateRule = privateRule;
	}

	public String getPrivateStage() {
		return privateStage;
	}

	public void setPrivateStage(String privateStage) {
		this.privateStage = privateStage;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public String getRoomPass() {
		return roomPass;
	}

	public void setRoomPass(String roomPass) {
		this.roomPass = roomPass;
	}

	@Persistent	
    String roomPass;
	
    public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getShutType() {
		return shutType;
	}

	public void setShutType(String shutType) {
		this.shutType = shutType;
	}

	@Persistent
	String shutType;		//news:エリア替えで終了		option：エリア替えでも終了しない
    
    @Persistent
    private String name;
    
    @Persistent
    private String uid;
     
    @Persistent
    private String comment;
    
    @Persistent
    private String passwd;
     
    @Persistent
    private Date startTime;
    
    @Persistent
    private Date endTime;

	public RoomData(String name, String uid, String comment, String passwd, Date startTime,
			Date endTime,String rule,String members,
			String privateRule,String privateStage,
			String roomPass,
			String shutType) {
		super();
		this.name = name;
		this.uid = uid;
		this.comment = comment;
		this.passwd = passwd;
		this.startTime = startTime;
		this.endTime = endTime;
		this.shutType=shutType;
		this.rule = rule;
		this.privateStage = privateStage;
		this.privateRule = privateRule;
		this.roomPass = roomPass;
		this.members = members;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
    
 
    
}

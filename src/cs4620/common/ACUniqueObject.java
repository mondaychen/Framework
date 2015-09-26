package cs4620.common;

public abstract class ACUniqueObject {
	private UUIDGenerator.ID _id = null;
	
	public void setID(UUIDGenerator.ID id) {
		_id = id;
	}
	public UUIDGenerator.ID getID() {
		return _id;
	}
}

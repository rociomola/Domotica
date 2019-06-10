package Base_COM_Serie;

public enum Ctrl_Persianas {
	PER_STOP,
	PER_DOWN,
	PER_UP,
	PER_STOP2;
	
	public static Ctrl_Persianas fromInteger(int x) {
        switch(x) {
        case 0:
            return PER_STOP;
        case 1:
            return PER_DOWN;
        case 2:
        	return PER_UP;
        case 3:
        	return PER_STOP2;
        }
        return null;
    }
}

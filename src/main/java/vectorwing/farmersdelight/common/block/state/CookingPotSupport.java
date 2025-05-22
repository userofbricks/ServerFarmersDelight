package vectorwing.farmersdelight.common.block.state;

import net.minecraft.util.StringIdentifiable;

public enum CookingPotSupport implements StringIdentifiable
{
	NONE("none"),
	TRAY("tray"),
	HANDLE("handle");

	private final String supportName;

	CookingPotSupport(String name) {
		this.supportName = name;
	}

	@Override
	public String toString() {
		return this.asString();
	}

	@Override
	public String asString() {
		return this.supportName;
	}
}

package icbm.zhapin.daodan;

public class DModule extends DaoDan
{
	public DModule(String name, int ID, int tier)
	{
		super(name, ID, tier);
	}

	public void onExplode(EDaoDan missileObj)
	{
		missileObj.dropMissileAsItem();
	}
}
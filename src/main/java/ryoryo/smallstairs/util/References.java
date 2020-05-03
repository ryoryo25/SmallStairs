package ryoryo.smallstairs.util;

public class References {
	public static final String MOD_ID = "smallstairs";
	public static final String MOD_NAME = "SmallStairs";

	public static final String MOD_VERSION_MAJOR = "GRADLE.VERSION_MAJOR";
	public static final String MOD_VERSION_MINOR = "GRADLE.VERSION_MINOR";
	public static final String MOD_VERSION_PATCH = "GRADLE.VERSION_PATCH";
	public static final String MOD_VERSION = MOD_VERSION_MAJOR + "." + MOD_VERSION_MINOR + "." + MOD_VERSION_PATCH;

	public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.5.2768,);"
												// + "required-after:polishedlib@[1.0.2,);"
												+ "required-after:polishedlib;"
												+ "after:quark;";

	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";

	public static final String PROXY_CLIENT = "ryoryo.smallstairs.proxy.ClientProxy";
	public static final String PROXY_COMMON = "ryoryo.smallstairs.proxy.CommonProxy";
}
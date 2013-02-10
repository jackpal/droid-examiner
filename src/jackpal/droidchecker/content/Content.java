package jackpal.droidchecker.content;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.opengl.GLES10;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Content {
	private static Context sContext;
	private static java.text.DateFormat sDateFormat;
	private static java.text.DateFormat sTimeFormat;

    /**
     * An array of sample (dummy) items.
     */
    public static List<Item> ITEMS = new ArrayList<Item>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

    private static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

	/**
     * A dummy item representing a piece of content.
     */
    public static abstract class Item {
        public final String id;
        private final String mLabel;

        public Item(String id, String label) {
            this.id = id;
            this.mLabel = label;;
        }

        public String getLabel() {
        	return mLabel;
        }

        public abstract String getContents();

        @Override
        public String toString() {
            return mLabel;
        }
    }

    private static class PIS extends Pair<Integer, String> {
    	public PIS(int i, String s) {
    		super(i, s);
    	}
    };

    private static String formatBitmask(int bits, List<PIS> l) {
    	StringBuilder sb = new StringBuilder();
    	Formatter f = new Formatter(sb);
    	String prefix = "";
    	for (PIS p: l) {
    		int mask = p.first;
    		if (mask != 0 && mask == (mask & bits)) {
    			f.format("%s%s", prefix, p.second);
    			bits &= ~mask;
    			prefix = "|";
    		}
    	}
    	if (bits != 0) {
    		f.format("%s0x%x", prefix, bits);
    	}
    	return sb.toString();
    }

    public static class BuildItem extends Item {

        public BuildItem(String id, String label) {
        	super(id, label);
        }

		public String getContents() {
			StringBuilder sb = new StringBuilder();
			Formatter f = new Formatter(sb, Locale.US);
			f.format("board: %s\n", Build.BOARD);
			f.format("bootloader: %s\n", Build.BOOTLOADER);
			f.format("brand: %s\n", Build.BRAND);
			f.format("cpu_abi: %s\n", Build.CPU_ABI);
			f.format("cpu_abi2: %s\n", Build.CPU_ABI2);
			f.format("device: %s\n", Build.DEVICE);
			f.format("display: %s\n", Build.DISPLAY);
			f.format("fingerprint: %s\n", Build.FINGERPRINT);
			f.format("hardware: %s\n", Build.HARDWARE);
			f.format("host: %s\n", Build.HOST);
			f.format("id: %s\n", Build.ID);
			f.format("manufacturer: %s\n", Build.MANUFACTURER);
			f.format("model: %s\n", Build.MODEL);
			f.format("product: %s\n", Build.PRODUCT);
			f.format("radio: %s\n", Build.RADIO);
			f.format("serial: %s\n", Build.SERIAL);
			f.format("tags: %s\n", Build.TAGS);
			f.format("time: %s %s\n", sDateFormat.format(Build.TIME), sTimeFormat.format(Build.TIME));
			f.format("type: %s\n", Build.TYPE);
			f.format("user: %s\n", Build.USER);

			String s = sb.toString();
			Log.i("BuildItem", s);
			return s;
        }
    }


    public static class RuntimeItem extends Item {

        public RuntimeItem(String id, String label) {
        	super(id, label);
        }

		public String getContents() {
			StringBuilder sb = new StringBuilder();
			Formatter f = new Formatter(sb, Locale.US);
			Runtime r = Runtime.getRuntime();
			f.format("availableProcessors: %d\n", r.availableProcessors());
			f.format("freeMemory: %s\n", formatFileSize(r.freeMemory()));
			f.format("totalMemory: %s\n", formatFileSize(r.totalMemory()));
			f.format("maxMemory: %s\n", formatFileSize(r.maxMemory()));

			return sb.toString();
        }
    }

    public static class ConfigurationInfoItem extends Item {
    	private final ConfigurationInfo mConfigurationInfo;

        public ConfigurationInfoItem(String id, String label, ConfigurationInfo info) {
        	super(id, label);
        	mConfigurationInfo = info;
        }

		public String getContents() {
			StringBuilder sb = new StringBuilder();
			Formatter f = new Formatter(sb, Locale.US);
			ConfigurationInfo c = mConfigurationInfo;
			f.format("ConfigurationInfo: %s\n", c);
			f.format("glEsVersion: %s\n", c.getGlEsVersion());
			f.format("inputFeatures: %x\n", c.reqInputFeatures);
			f.format("keyboardType: %x\n", c.reqKeyboardType);
			f.format("navigation: %x\n", c.reqNavigation);
			f.format("touchScreen: %x\n", c.reqTouchScreen);
			return sb.toString();
        }
    }

    public static class ConfigItem extends Item {
    	private static final String TAG = "ConfigItem";
        private final String mContents;

        public ConfigItem(String id, String label, Context context) {
        	super(id, label);
            mContents = getConfig(context);
            Log.i(TAG, mContents);
        }

        private String getConfig(Context context) {
			StringBuilder sb = new StringBuilder();
			Configuration config = context.getResources().getConfiguration();
			Locale locale = config.locale;
			Formatter f = new Formatter(sb, locale);
			f.format("Config: %s\n", config);
			// API level 17 f.format("densityDpi = %d", config.densityDpi);
			f.format("fontScale: %g\n", config.fontScale);
			f.format("hardKeyboardHidden: %d\n", config.hardKeyboardHidden);
			f.format("keyboard: %d\n", config.keyboard);
			f.format("keyboardHidden: %d\n", config.keyboardHidden);
			f.format("locale: %s\n", config.locale);
			f.format("mcc: %d\n", config.mcc);
			f.format("mnc: %d\n", config.mnc);
			f.format("navigation: %d\n", config.navigation);
			f.format("navigationHidden: %d\n", config.navigationHidden);
			f.format("orientation: %d\n", config.orientation);
			f.format("screenHeightDp: %d\n", config.screenHeightDp);
			f.format("screenLayout: 0x%x\n", config.screenLayout);
			f.format("screenWidthDp: %d\n", config.screenWidthDp);
			f.format("smallestScreenWidthDp: %d\n", config.smallestScreenWidthDp);
			f.format("touchscreen: %d\n", config.touchscreen);
			f.format("uiMode: %s\n", formatBitmask(config.uiMode, Arrays.asList(
					new PIS(Configuration.UI_MODE_TYPE_UNDEFINED, "UI_MODE_TYPE_UNDEFINED"),
					new PIS(Configuration.UI_MODE_TYPE_NORMAL, "UI_MODE_TYPE_NORMAL"),
					new PIS(Configuration.UI_MODE_TYPE_DESK, "UI_MODE_TYPE_DESK"),
					new PIS(Configuration.UI_MODE_TYPE_CAR, "UI_MODE_TYPE_CAR"),
					new PIS(Configuration.UI_MODE_TYPE_TELEVISION, "UI_MODE_TYPE_TELEVISION"),
					new PIS(Configuration.UI_MODE_TYPE_APPLIANCE, "UI_MODE_TYPE_APPLIANCE"),
					new PIS(Configuration.UI_MODE_NIGHT_NO, "UI_MODE_NIGHT_NO"),
					new PIS(Configuration.UI_MODE_NIGHT_YES, "UI_MODE_NIGHT_YES")
					)));
			return sb.toString();
        }

		public String getContents() {
        	return mContents;
        }
    }

    public static class OpenGLItem extends Item {

        public OpenGLItem(String id, String label) {
        	super(id, label);
        }

		public String getContents() {
			StringBuilder sb = new StringBuilder();
			Formatter f = new Formatter(sb, Locale.US);
			String vendor = GLES10.glGetString(GLES10.GL_VENDOR);
			if (vendor == null) {
				f.format("!!! Please tap on another tab and then tap back on this tab. !!!\n\n");
			}
			f.format("vendor: %s\n", vendor);
			f.format("version: %s\n", GLES10.glGetString(GLES10.GL_VERSION));
			String extensions = GLES10.glGetString(GLES10.GL_EXTENSIONS);
			if (extensions == null) {
				extensions = "";
			}
			f.format("extensions: %s\n", extensions.replaceAll(" ", "\n"));
			return sb.toString();
        }
    }

    public static class FileInfo extends Item {
    	private String mPath;

        public FileInfo(String id, String label, String path) {
        	super(id, label);
        	mPath = path;
        }

		public String getContents() {
			return mPath + ":\n" + readFile(mPath);
		}
    }

    static String readFile(String path) {
		BufferedReader reader;
		try {
			reader = new BufferedReader( new FileReader (path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "Could not open " + path;
		}
		StringBuilder sb = new StringBuilder();
		String line;
	    String ls = System.getProperty("line.separator");

	    try {
		    while( ( line = reader.readLine() ) != null ) {
		        sb.append( line );
		        sb.append( ls );
		    }
	    } catch (IOException e) {
	    	sb.append("--- io exception: " + e);
	    }

		return sb.toString();
    }

	public static void init(Context context) {
        ITEMS.clear();
        ITEM_MAP.clear();

		sContext = context;
		sDateFormat = DateFormat.getDateFormat(context);
		sTimeFormat = DateFormat.getTimeFormat(context);
		int id = 1;
        addItem(new BuildItem(Integer.toString(id++), "Build"));
        addItem(new FileInfo(Integer.toString(id++), "CmdLine", "/proc/cmdline"));
        addItem(new ConfigItem(Integer.toString(id++), "Configuration", context));
        final ActivityManager activityManager =
        	    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    	final ConfigurationInfo configurationInfo =
    	    activityManager.getDeviceConfigurationInfo();
        addItem(new ConfigurationInfoItem(Integer.toString(id++), "ConfigurationInfo", configurationInfo));
        addItem(new FileInfo(Integer.toString(id++), "CpuInfo", "/proc/cpuinfo"));
        addItem(new FileInfo(Integer.toString(id++), "Crypto", "/proc/crypto"));
        addItem(new FileInfo(Integer.toString(id++), "Devices", "/proc/devices"));
        addItem(new FileInfo(Integer.toString(id++), "DiskStats", "/proc/diskstats"));
        addItem(new FileInfo(Integer.toString(id++), "Filesystems", "/proc/filesystems"));
        addItem(new FileInfo(Integer.toString(id++), "Kernel Version", "/proc/version"));
        addItem(new FileInfo(Integer.toString(id++), "LoadAvg", "/proc/loadavg"));
        addItem(new FileInfo(Integer.toString(id++), "Modules", "/proc/modules"));
        addItem(new OpenGLItem(Integer.toString(id++), "OpenGL"));
        addItem(new RuntimeItem(Integer.toString(id++), "Runtime"));
        addItem(new FileInfo(Integer.toString(id++), "Uptime", "/proc/uptime"));
	}

	private static String formatFileSize(long n) {
		return android.text.format.Formatter.formatFileSize(sContext, n);
	}
}

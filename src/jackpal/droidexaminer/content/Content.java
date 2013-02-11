package jackpal.droidexaminer.content;

import java.io.BufferedReader;
import java.io.File;
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
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLES10;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.InputDevice;
import android.view.WindowManager;

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

    private static String formatBitmask(int bits, List<PIS> l, boolean allowOverlapping) {
        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        String prefix = "";
        for (PIS p: l) {
            int mask = p.first;
            if (mask != 0 && mask == (mask & bits)) {
                f.format("%s%s", prefix, p.second);
                if (! allowOverlapping) {
                    bits &= ~mask;
                }
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
            // f.format("radio: %s\n", Build.RADIO);
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
                    ), false));
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

    public static class InputDeviceInfo extends Item {
        public InputDeviceInfo(String id, String label) {
            super(id, label);
        }

        public String getContents() {
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb, Locale.US);
            int[] ids = InputDevice.getDeviceIds();
            f.format("Device count: %d\n", ids.length);
            for (int i = 0; i < ids.length; i++) {
                int id = ids[i];
                InputDevice device = InputDevice.getDevice(id);
                f.format("#%d: id = 0x%x\n%s\n", i, id, device);
            }
            return sb.toString();
        }
    }


    public static class SensorsItem extends Item {
        final private SensorManager mManager;
        public SensorsItem(String id, String label, SensorManager manager) {
            super(id, label);
            mManager = manager;
        }

        public String getContents() {
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb, Locale.US);
            List<Sensor> sensors = mManager.getSensorList(Sensor.TYPE_ALL);
            f.format("Sensor count: %d\n", sensors.size());
            for (int i = 0; i < sensors.size(); i++) {
                Sensor s = sensors.get(i);
                f.format("#%d: %s\n", i, s);
            }
            return sb.toString();
        }
    }

    public static class BatteryItem extends Item {
        public BatteryItem(String id, String label) {
            super(id, label);
        }

        public String getContents() {
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb, Locale.US);
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = sContext.registerReceiver(null, ifilter);
            f.format("present: %b\n", batteryStatus.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false));
            f.format("technology: %s\n", batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
            f.format("status: %d\n", batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1));
            f.format("plugged: %d\n", batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1));
            f.format("health: %d\n", batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1));
            f.format("level: %d of %d\n", batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1),
                    batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1));
            f.format("temperature: %d\n", batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1));
            f.format("voltage: %d\n", batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1));
            return sb.toString();
        }
    }

    public static class CamerasItem extends Item {
        public CamerasItem(String id, String label) {
            super(id, label);
        }

        public String getContents() {
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb, Locale.US);
            int cameraCount = Camera.getNumberOfCameras();
            f.format("Camera count: %d\n", cameraCount);
            for (int i = 0; i < cameraCount; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                f.format("#%d: %s\n", i, info);
                // f.format("canDisableShutterSound: %b\n", info.canDisableShutterSound);
                f.format("facing: %d\n", info.facing);
                f.format("orientation: %d\n", info.orientation);
            }
            return sb.toString();
        }
    }

    public static class DisplayMetricsItem extends Item {
        public DisplayMetricsItem(String id, String label) {
            super(id, label);
        }

        public String getContents() {
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb, Locale.US);
            WindowManager windowManager = (WindowManager)sContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            f.format("%s\n", display);
            return sb.toString();
        }
    }

    public static class StorageItem extends Item {
        public StorageItem(String id, String label) {
            super(id, label);
        }

        public String getContents() {
            StringBuilder sb = new StringBuilder();
            Formatter f = new Formatter(sb, Locale.US);
            formatStorage(f, Environment.getRootDirectory());
            formatStorage(f, Environment.getDataDirectory());
            f.format("External storage is emulated: %s\n", Environment.isExternalStorageEmulated());
            f.format("External storage is removable: %s\n", Environment.isExternalStorageRemovable());
            f.format("External storage state: %s\n", Environment.getExternalStorageState());
            formatStorage(f, Environment.getExternalStorageDirectory());
            return sb.toString();
        }

        private void formatStorage(Formatter f, File file) {
            String path = file.getPath();
            StatFs stat = new StatFs(path);
            long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount();
            f.format("path: %s size: %s\n", path, formatFileSize(bytesAvailable));
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
        addItem(new BatteryItem(Integer.toString(id++), "Battery"));
        addItem(new CamerasItem(Integer.toString(id++), "Cameras"));
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
        addItem(new DisplayMetricsItem(Integer.toString(id++), "Display"));
        addItem(new FileInfo(Integer.toString(id++), "Filesystems", "/proc/filesystems"));
        addItem(new InputDeviceInfo(Integer.toString(id++), "Input Devices"));
        addItem(new FileInfo(Integer.toString(id++), "Kernel Version", "/proc/version"));
        addItem(new FileInfo(Integer.toString(id++), "LoadAvg", "/proc/loadavg"));
        addItem(new FileInfo(Integer.toString(id++), "Memory", "/proc/meminfo"));
        addItem(new FileInfo(Integer.toString(id++), "Modules", "/proc/modules"));
        addItem(new OpenGLItem(Integer.toString(id++), "OpenGL"));
        addItem(new RuntimeItem(Integer.toString(id++), "Runtime"));
        addItem(new SensorsItem(Integer.toString(id++), "Sensors", (SensorManager) context.getSystemService(Context.SENSOR_SERVICE)));
        addItem(new StorageItem(Integer.toString(id++), "Storage"));
        addItem(new FileInfo(Integer.toString(id++), "Uptime", "/proc/uptime"));
    }

    private static String formatFileSize(long n) {
        return android.text.format.Formatter.formatFileSize(sContext, n);
    }
}

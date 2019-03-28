package com.lkl.ansuote.hdqlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用工具类
 *
 * @author huangdongqiang
 * @date 2018/3/30
 */
public class Utils {

    /**
     * 安全转换成 Int 型
     * @param value
     * @param defaultValue
     * @return
     */
    public static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * 安全转换成 double 型
     * @param value
     * @param defaultValue
     * @return
     */
    public static double convertToDouble(Object value, double defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }

        try {
            return Double.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全转换成 float 型
     * @param value
     * @param defaultValue
     * @return
     */
    public static float convertToFloat(Object value, float defaultValue) {
        if (null == value || "".equals(value.toString().trim())) {
            return defaultValue;
        }

        try {
            return Float.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全转换成 long 型
     * @param value
     * @param defaultValue
     * @return
     */
    public static long convertToLong(Object value, long defaultValue) {
        if (null == value || "".equals(value.toString().trim())) {
            return defaultValue;
        }

        try {
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将对象序列化到本地
     * @param path
     * @param saveObject
     */
    public static final void saveObject(String path, Object saveObject) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File f = new File(path);
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(saveObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从本地反序列化取出对象
     * @param path
     * @return
     */
    public static final Object restoreObject(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object object = null;
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
            return object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    /**
     * 判断app是否安装了
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        return 0 != getAppVersionCode(context, packageName);
    }


    /**
     * 获取App的版本号
     * @param context
     * @param packageName
     * @return
     */
    public static int getAppVersionCode(Context context, String packageName) {
        PackageInfo packageInfo = null;
        int versionCode = 0;
        try {
            if (null != context && null != packageName && !"".equals(packageName)) {
                packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                if(null != packageInfo) {
                    versionCode = packageInfo.versionCode;
                }
            }
        } catch (Exception e) {

        }
        return versionCode;
    }


    /**
     * 通过 drawable id 获取对应图片的路径
     * @param context
     * @param resource
     * @return
     */
    public static String getDrawablePath(Context context, int resource) {
        String filePath = null;
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            //先复制到/data/包名/files/ 目录下
            String dir = context.getFilesDir().getPath();
            if (!TextUtils.isEmpty(dir)) {
                File file = new File(dir, context.getResources().getResourceEntryName(resource) + ".png");
                if (null != file) {
                    if (file.exists()) {
                        return file.getAbsolutePath();
                    } else {
                        inputStream = context.getResources().openRawResource(resource);
                        if (null != inputStream) {
                            fos = new FileOutputStream(file.getAbsoluteFile());

                            byte[] buffer = new byte[8192];
                            int count;
                            // 开始复制Logo图片文件
                            while((count=inputStream.read(buffer)) > 0)
                            {
                                fos.write(buffer, 0, count);
                            }
                            filePath = file.getAbsolutePath();
                        }

                    }


                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return filePath;
    }


    /**
     * 隐藏输入法
     * @param activity
     */
    public static void hideSoftInputFromWindow(Activity activity){
        if (null != activity) {
            Window window = activity.getWindow();
            if (null != window) {
                View decorView = window.getDecorView();
                if (null != decorView) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (null != imm) {
                        //隐藏软键盘 //
                        imm.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
                    }
                }
            }
        }
    }


    /**
     * 输入法反转,原本是打开状态的，则隐藏；原本是关闭状态的，则打开。
     * @param activity
     */
    public static void toggleSoftInputFromWindow(Activity activity) {
        if (null != activity) {
            Window window = activity.getWindow();
            if (null != window) {
                View decorView = window.getDecorView();
                if (null != decorView) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (null != imm) {
                        imm.toggleSoftInputFromWindow(decorView.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
    }

    /**
     * 获取纯颜色的bitmap
     * @param width
     * @param height
     * @param color
     * @return
     */
    public static Bitmap getColormBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (null != bitmap) {
            bitmap.eraseColor(color);
        }
        return bitmap;
    }

    /**
     * 把对象序列化到本地
     * @param object
     * @param localPath
     * @return  true 写入成功 ; false 写入失败
     */
    public static boolean writeObjToFile(Object object, String localPath) {
        if (TextUtils.isEmpty(localPath)) {
            return false;
        }

        ObjectOutputStream oos = null;
        try {
            //目录不存在则创建目录
            File dir = new File(localPath);
            if (null != dir && !dir.exists()) {
                dir.mkdirs();
            }

            //删除之前的文件
            File file = new File(localPath);
            if (null != file && file.exists()) {
                file.delete();
            }

            //写入新数据
            oos = new ObjectOutputStream(new FileOutputStream(new File(localPath)));
            if (null != oos) {
                oos.writeObject(object);
            }

            return true;
        } catch (Exception e) {

        } finally {
            try {
                if (null != oos) {
                    oos.close();
                    oos = null;
                }
            }
            catch (Exception e)
            {
            }
        }

        return false;
    }


    /**
     * 从本地反序列化出对象
     * @param localPath
     * @return
     */
    public static Object readObjFromFile(String localPath) {
        if (TextUtils.isEmpty(localPath)) {
            return null;
        }

        Object obj = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(localPath)));
            if (null != ois) {
                obj = ois.readObject();
            }
        } catch (Exception e) {

        } finally {
            try {
                if (null != ois) {
                    ois.close();
                    ois = null;
                }
            } catch (Exception e) {

            }
        }
        return obj;
    }

    /**
     * 合并数组
     * @param first
     * @param rest
     * @param <T>
     * @return
     */
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * 合并字节数组
     * @param values
     * @return
     */
    public static byte[] concatByte(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    // 保存Bitmap到本地
    public static void saveBitmap(Bitmap bitmap, String filePath) {
        try {
            FileOutputStream output = new FileOutputStream(filePath);
            // 保存图像，第一个参数表示保存的格式，第二个参数表示保存的质量，第三个参数表示保存的位置
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 把图片压缩到指定大小
    public static Bitmap revitionImageSize(String path, int max) {
        // 解析原始图像
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap map = BitmapFactory.decodeFile(path, options);
        int widthRatio = (int) Math.ceil(options.outWidth / 500);
        int heightRatio = (int) Math.ceil(options.outHeight / 500);
        options.inSampleSize = 1;
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                // inSampleSize配置最后显示的像素比例，最后显示的大小为原图的1/inSampleSize，注意inSampleSize一定要大于1
                options.inSampleSize = widthRatio;
            } else {
                options.inSampleSize = heightRatio;
            }
        }
        // 关闭解析边框的模式，以便于根据配置获取像素信息
        options.inJustDecodeBounds = false;
        map = BitmapFactory.decodeFile(path, options);
        // 取得经过第一次压缩后的宽高
        int Width = map.getWidth();
        int Height = map.getHeight();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        try {
            bao.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap newMap = null;
        int times = 0;
        while (bao.toByteArray().length > max) {
            bao.reset();
            times++;
            if (newMap != null) {
            }
            newMap = Bitmap.createScaledBitmap(map,
                    (int) (Width * (1 - times * 0.15)),
                    (int) (Height * (1 - times * 0.15)), true);
            newMap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            try {
                bao.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bao.reset();
        try {
            bao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (newMap == null) {
            return map;
        } else {
            return newMap;
        }

    }

    /**
     * 获取到"/data/data/<package name>/files/dirName”
     * /data/user/0/com.lkl.ansuote.postwidget/files/abc
     *
     * 这个方法获取的目录不是在sdcard上，而是在应用安装的目录，不需要权限
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFilePath(Context context, String fileName) {
        if (null != context && !TextUtils.isEmpty(fileName)) {
            return context.getFilesDir().getPath() + File.separator + fileName;
        }
        return null;
    }

    /**
     * 获取到"/data/data/<package name>/cache/fileName”
     * /data/user/0/com.lkl.ansuote.postwidget/cache/abc
     *
     * 这个方法获取的目录不是在sdcard上，而是在应用安装的目录，
     * 这个文件里面的数据在设备内存不足的时候，会被系统删除数据。
     * @param context
     * @param fileName
     * @return
     */
    public static String getCachePath(Context context, String fileName) {
        if (null != context && !TextUtils.isEmpty(fileName)) {
            return context.getCacheDir().getPath() + File.separator + fileName;
        }

        return null;
    }

    /**
     * 获取 SDCard/Android/data/你的应用的包名/files/fileName/ 目录，一般放一些长时间保存的数据
     * /storage/emulated/0/Android/data/com.lkl.ansuote.postwidget/files/abc
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getExternalFilesPath(Context context, String fileName) {
        if (null != context && !TextUtils.isEmpty(fileName)) {
            return context.getExternalFilesDir(fileName).getPath();
        }
        return null;
    }

    /**
     * 获取 SDCard/Android/data/你的应用包名/cache/fileName
     * /storage/emulated/0/Android/data/com.lkl.ansuote.postwidget/cache/abc
     *
     * 这个方法获取的目录不是在sdcard上，而是在应用安装的目录
     * @param context
     * @param fileName
     * @return
     */
    public static String getExternalCachePath(Context context, String fileName) {
        if (null != context && !TextUtils.isEmpty(fileName)) {
            return context.getExternalCacheDir().getPath() + File.separator + fileName;
        }
        return null;
    }


    /**
     * 读取本地文件中JSON字符串
     *
     * @param fileName
     * @return
     */
    public static String getJsonFromAssets(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }



    /**
     * html格式兼容
     * @param html
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }


    /**
     * 将集合按指定数量分组
     * @param list 数据集合
     * @param quantity 分组数量
     * @return 分组结果
     */
    public static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (list == null || list.size() == 0) {
            return null;
        }

        if (quantity <= 0) {
            new IllegalArgumentException("Wrong quantity.");
        }

        List<List<T>> wrapList = new ArrayList<List<T>>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList<T>(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity)));
            count += quantity;
        }

        return wrapList;
    }


    /**
     * 大数输出，3位逗号隔开的字符串，有小数的时候，保留两位有效数字
     * @param money
     * @return 12,000 或者 12,000.05
     */
    public static String getBigDecimalForSeparator(double money) {
        try {
            BigDecimal bigDecimal = new BigDecimal(money);
            if (null != bigDecimal) {
                //转化成不显示科学计数法
                double moneyFormat = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                String pattern = "#,###";
                if ((moneyFormat - (int) moneyFormat) > 0) {
                    //存在小数
                    pattern = "#,###.00";
                }
                DecimalFormat df = new DecimalFormat(pattern);
                return df.format(moneyFormat);
            }

        } catch (Exception e) {

        }
        return String.valueOf(money);
    }

    /**
     * 大数输出 - 不使用科学技术法
     * @param money
     * @param newScale  保留小数点后几位
     * @return
     */
    public static String getBigDecimal(double money, int newScale) {
        try {

            BigDecimal bigDecimal = new BigDecimal(money);
            if (null != bigDecimal) {
                return bigDecimal.setScale(newScale, BigDecimal.ROUND_HALF_UP).toPlainString();
            }
        } catch (Exception e) {
        }

        return "";
    }

    /**
     * 大数输出 - 不使用科学技术法 默认保留两位
     * @param money
     * @return
     */
    public static String getBigDecimal(double money) {
        try {
            BigDecimal bigDecimal = new BigDecimal(money);
            if (null != bigDecimal) {
                return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            }

        } catch (Exception e) {
        }

        return "";
    }
}

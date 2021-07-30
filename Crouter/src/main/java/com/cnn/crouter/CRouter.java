package com.cnn.crouter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.cnn.crouter.thread.DefaultPoolExecutor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import dalvik.system.DexFile;

/**
 * Created by caining on 7/29/21 16:09
 * E-Mail Address：cainingning@360.cn
 */
public class CRouter {
    private volatile static CRouter instance = null;
    private volatile static boolean hasInit = false;
    private static Application application;
    public static final String ROUTE_ROOT_PAKCAGE = "com.cnn.crouter";
    private static Map<String ,String> mapHolder = new HashMap<>();

    /**
     * Init, it must be call before used router.
     */
    public static void init(Application application) {
        if (!hasInit) {
            CRouter.application=application;
            hasInit=true;
            try {
                getFileNameByPackageName(application, ROUTE_ROOT_PAKCAGE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Get instance of router. A
     * All feature U use, will be starts here.
     */
    public static CRouter getInstance() {
        if (!hasInit) {
            throw new InitException("ARouter::Init::Invoke init(context) first!");
        } else {
            if (instance == null) {
                synchronized (CRouter.class) {
                    if (instance == null) {
                        instance = new CRouter();
                    }
                }
            }
            return instance;
        }
    }


    public void navigation(String path) {
         startActivity(path);
    }

    private void startActivity(String path) {
        String classPath
                = mapHolder.get(path);
        if (!TextUtils.isEmpty(classPath)) {
            Intent intent = new Intent();
            intent.setClassName(application, classPath);//设置包路径
            ActivityCompat.startActivity(application, intent, null);
        }else {
            Toast.makeText(application, "路径空啦", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 通过指定包名，扫描包下面包含的所有的ClassName
     *
     * @param context     U know
     * @param packageName 包名
     * @return 所有class的集合
     */
    private static Set<String> getFileNameByPackageName(Context context, final String packageName) throws PackageManager.NameNotFoundException, IOException, InterruptedException {
        final Set<String> classNames = new HashSet<>();

        List<String> paths = getSourcePaths(context);
        final CountDownLatch parserCtl = new CountDownLatch(paths.size());

        for (final String path : paths) {
            DefaultPoolExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    DexFile dexfile = null;

                    try {
                        if (path.endsWith("EXTRACTED_SUFFIX")) {
                            //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                            dexfile = DexFile.loadDex(path, path + ".tmp", 0);
                        } else {
                            dexfile = new DexFile(path);
                        }

                        Enumeration<String> dexEntries = dexfile.entries();
                        while (dexEntries.hasMoreElements()) {
                            String className = dexEntries.nextElement();
                            if (className.startsWith(packageName)) {
                                classNames.add(className);
                                try {
                                    Class clazz = Class.forName(className);
                                    Object obj = clazz.newInstance();
                                    Field field03 = clazz.getDeclaredField("holder"); // 获取属性为id的字段
                                    String value= (String) field03.get(obj);
                                    String[] split = value.split(":");
                                    if (split!=null&&split.length==2) {
                                        mapHolder.put(split[0],split[1]);
                                    }
                                    Log.i("test-->",mapHolder.toString());
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Throwable ignore) {
                        Log.e("ARouter", "Scan map file in dex files made error.", ignore);
                    } finally {
                        if (null != dexfile) {
                            try {
                                dexfile.close();
                            } catch (Throwable ignore) {
                            }
                        }

                        parserCtl.countDown();
                    }
                }
            });
        }

        parserCtl.await();

        return classNames;
    }


    private static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir); //add the default apk path
        return sourcePaths;
    }
}

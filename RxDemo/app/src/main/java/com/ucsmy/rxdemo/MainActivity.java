package com.ucsmy.rxdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ucsmy.xxtea.XXTEA;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.observers.FutureObserver;
import io.reactivex.internal.operators.single.SingleCache;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView rxText;
    private ImageView img;
    private static final String TAG = "---RXDemo---";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxText = (TextView) findViewById(R.id.rxText);
        img = (ImageView) findViewById(R.id.img);

        String key = "lalalal234z@$%JJ";

          String filepaht = "/storage/emulated/0/Pictures/1469417296713.jpg";
       /*String filename = XXTEA.encryptFile(this,new File(filepaht),key);
        rxText.setText(filename);

       byte[] byt = XXTEA.decryptFile(this,filename,key);
        Bitmap bit = BitmapFactory.decodeByteArray(byt,0,byt.length);
        img.setImageBitmap(bit);
*/
        String filepaht2 = "/storage/emulated/0/pic/";

        String finame = XXTEA.encryptFile(this,new File(filepaht),key);
        StringBuffer sb = new StringBuffer();
        sb.append(finame);
        byte[] byt2 = XXTEA.decryptFile(finame,key);

        Bitmap bit = BitmapFactory.decodeByteArray(byt2,0,byt2.length);
        img.setImageBitmap(bit);
        rxText.setText(sb.toString());



       /*
        String enstring = "lalalal234z@$%JJ";
        byte[] ba = XXTEA.encrypt(enstring,key);
        String destring = XXTEA.decryptToString(ba,key);
        rxText.setText(destring);
        */


        //  demo1();
        // demo2();
        // demo3();
      // demo4();
       // demo5();
    }

    /**
     * xxtea对文件进行加密解密
     * 目前已有的方式为：对byte或加密解密后为byte或string
     *
     * 方法改造：
     * 加密方式
     * 1，传入文件名称，转为byte[]加密。。
     * 2，加密后得到byte，写入文件
     * 3，返回写入的文件名称
     *
     *
     * 解密方式
     * 1，读取文件地址，转为byte[]
     * 2，解密得到byte[]
     * 3，byte写入文件，得到正确的文件
     */

    private void demo5(){
        String[] strs = {"aaa", "bbb", "ccc"};

        Observable.fromArray(strs).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s.toUpperCase();
            }
        }).flatMap(new Function<String, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(final String s) throws Exception {
                Log.d(TAG,s);
                return  Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext(s);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object value) {
                Log.d(TAG,"--"+value.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void demo4() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("11");
                e.onComplete();
            }
        }).map(new Function<String, Boolean>() {

            @Override
            public Boolean apply(String s) throws Exception {
                return s.contains("2");
            }
        }).filter(new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean aBoolean) throws Exception {
                Log.d(TAG,aBoolean+"----BB");
                return aBoolean;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG,aBoolean+"----AA");
            }
        });
    }

    private List<String> lists = new ArrayList<String>();

    private void demo3() {
        String[] strs = {"aaa", "bbb", "ccc"};
        Observable.fromArray(strs).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s.toUpperCase();
            }
        }).toList().map(new Function<List<String>, List<String>>() {
            @Override
            public List<String> apply(List<String> strings) throws Exception {
                Collections.reverse(strings);
                return strings;
            }
        }).subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<List<String>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d(TAG,"d=="+d.isDisposed());
                }

                @Override
                public void onSuccess(List<String> value) {
                    Log.d(TAG,"d=="+value.toString());
                    rxText.setText(value.toString());
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG,"d=="+e.getMessage());
                }
            });
    }

    /**
     * map
     */
    private void demo2() {

        Observable.just("12345").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return s.length();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Disposable--" + d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, value + "");
                rxText.setText(value + "");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });

    }

    /**
     * Rx操作符create
     */
    private void demo1() {

        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("1111");
                e.onNext("2222");
                e.onNext("3333");
                e.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe---" + d.isDisposed());

            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, value);
                rxText.setText(value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage() + "");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "oncomplete");
            }
        });
    }
}

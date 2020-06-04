package com.lody.virtual.client.hook.proxies.content;
import android.os.IInterface;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import mirror.android.app.ActivityThread;
import mirror.android.app.ContextImpl;
import mirror.android.content.ContentResolver;
import mirror.android.content.IContentService;
public class ContentResolverStub extends MethodInvocationProxy<MethodInvocationStub<Object>> {
    public ContentResolverStub() {
        super(new MethodInvocationStub<Object>(getContentResolver()));
    }
    private static Object getContentResolver() {
        Object os = ContentResolver.ctor.newInstance();
        return os;
    }
    @Override
    protected void onBindMethods() {
        super.onBindMethods();
    }
    @Override
    public void inject() throws Throwable {
    }
    @Override
    public boolean isEnvBad() {
        return getContentResolver() != getInvocationStub().getProxyInterface();
    }
}

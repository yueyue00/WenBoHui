package com.hebg3.wl.net;

public interface OnEntityLoadCompleteListener<T> extends OnErrorListener {
	void onEntityLoadComplete(Base base);

	void onError(T entity);
}

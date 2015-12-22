package com.yeliang.photogalleryactivity.core;

import java.io.Closeable;

public class CloseUtils
{
	/**
	 * �ر�Closeable����ֻҪʵ���˸ýӿڵ��඼��ʹ�ø÷������йر�
	 * ��100�����ʵ����Closeable�ӿ�
	 */
	public static void close(Closeable closeable)
	{
		try
		{
			if(closeable != null)
			{
				closeable.close();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

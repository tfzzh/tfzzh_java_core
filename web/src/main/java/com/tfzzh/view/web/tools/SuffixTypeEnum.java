/**
 * @author XuWeijie
 * @datetime 2015年9月6日_下午5:35:16
 */
package com.tfzzh.view.web.tools;

/**
 * 后缀名类型
 * 
 * @author XuWeijie
 * @datetime 2015年9月6日_下午5:35:16
 */
public enum SuffixTypeEnum {

	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:09
	 */
	Fax("image/fax"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:09
	 */
	Gif("image/gif"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:10
	 */
	Ico("image/x-icon"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:10
	 */
	Jfif("image/jpeg"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:11
	 */
	Jpe("image/jpeg"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:11
	 */
	Jpeg("image/jpeg"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:12
	 */
	Jpg("image/jpeg"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:13
	 */
	Net("image/pnetvue"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:13
	 */
	Png("image/png"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:14
	 */
	Rp("image/vnd.rn-realpix"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:14
	 */
	Tif("image/tiff"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:15
	 */
	Tiff("image/tiff"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:16
	 */
	Wbmp("image/vnd.wap.wbmp"),
	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:49:03
	 */
	Default("image/jpeg") {

		/**
		 * @author XuWeijie
		 * @datetime 2015年9月6日_下午5:56:32
		 * @return 后缀名
		 * @see com.tfzzh.view.web.tools.SuffixTypeEnum#getSufName()
		 */
		@Override
		public String getSufName() {
			return "jpg";
		}
	};

	/**
	 * 类型名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:38:49
	 */
	private final String type;

	/**
	 * 后缀名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:42:19
	 */
	private final String sufName;

	/**
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:39:06
	 * @param type 类型名
	 */
	SuffixTypeEnum(final String type) {
		this.type = type;
		this.sufName = this.name().toLowerCase();
	}

	/**
	 * 得到类型名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:41:25
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 得到后缀名
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:56:05
	 * @return the sufName
	 */
	public String getSufName() {
		return this.sufName;
	}

	/**
	 * 根据类型名得到类型对象
	 * 
	 * @author XuWeijie
	 * @datetime 2015年9月6日_下午5:42:51
	 * @param type 类型名
	 * @return 类型对象
	 */
	public static SuffixTypeEnum getType(String type) {
		type = type.toLowerCase();
		for (final SuffixTypeEnum ste : SuffixTypeEnum.values()) {
			if (ste.sufName.equals(type)) {
				return ste;
			}
		}
		return Default;
	}
}

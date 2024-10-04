package ci.dgmp.sigomap.sharedmodule.utilities;

import java.text.Normalizer;

public class StringUtils
{
	public static String stripAccents(String string)
	{
		if(string==null) return null;
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		string = string.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return string;
	}
	
	public static String stripAccentsToUpperCase(String string)
	{
		if(string==null) return "";
		return stripAccents(string).toUpperCase().trim();
	}
	
	public static String stripAccentsToLowerCase(String string)
	{
		if(string==null) return "";
		return stripAccents(string).toLowerCase().trim();
	}

	public static String blankToNull(String str)
	{
		return str == null ? null : str.trim().equals("") ? null : str;
	}

	public static boolean containsIgnoreCase(String baseStr, String searchedStr)
	{
		if(baseStr == null || searchedStr == null) return false;
		return baseStr.toLowerCase().contains(searchedStr.toLowerCase());
	}

	public static boolean containsIgnoreCaseAndAccents(String baseString, String searchedStr)
	{
		if(baseString == null || searchedStr == null) return false;
		return baseString.toLowerCase().contains(searchedStr.toLowerCase()) ? true : stripAccentsToLowerCase(baseString).contains(stripAccentsToLowerCase(searchedStr));
	}
}
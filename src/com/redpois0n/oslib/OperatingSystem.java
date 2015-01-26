package com.redpois0n.oslib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public enum OperatingSystem {

	WINDOWS,
	OSX,
	LINUX,
	SOLARIS,
	FREEBSD,
	OPENBSD,
	ANDROID,
	UNKNOWN;

	private static String shortName;
	private static String longName;

	/**
	 * Gets the operating system (Not linux distro)
	 * @param str String like System.getProperty("os.name");
	 * @return
	 */
	public static OperatingSystem getOperatingSystem(String str) {
		str = str.toLowerCase();

		OperatingSystem os;

		if (str.contains("win")) {
			os = OperatingSystem.WINDOWS;
		} else if (str.contains("mac")) {
			os = OperatingSystem.OSX;
		} else if (str.contains("linux")) {
			os = OperatingSystem.LINUX;
		} else if (str.contains("solaris") || str.contains("sunos")) {
			os = OperatingSystem.SOLARIS;
		} else if (str.contains("freebsd")) {
			os = OperatingSystem.FREEBSD;
		} else if (str.contains("android")) {
			os = OperatingSystem.ANDROID;
		} else if (str.contains("openbsd")) {
			os = OperatingSystem.OPENBSD;
		} else {
			os = OperatingSystem.UNKNOWN;
		}

		return os;
	}

	/**
	 * Gets this machines operating system
	 * @return
	 */
	public static OperatingSystem getOperatingSystem() {
		return getOperatingSystem(System.getProperty("os.name"));
	}
	
	/**
	 * Returns current linux distribution
	 * @return null if not running on Linux
	 */
	public static Distro getDistro() {
		String search = getShortOperatingSystem();
		Distro distro;
		
		if (OperatingSystem.getOperatingSystem() != LINUX) {
			return null;
		}
		
		if (search.toLowerCase().contains("ubuntu")) {
			distro = Distro.UBUNTU;
		} else if (search.toLowerCase().contains("kali") || search.toLowerCase().contains("backtrack")) {
			distro = Distro.KALI;
		} else if (search.toLowerCase().contains("centos")) {
			distro = Distro.CENTOS;
		} else if (search.toLowerCase().contains("debian")) {
			distro = Distro.DEBIAN;
		} else if (search.toLowerCase().contains("elementary")) {
			distro = Distro.ELEMENTARYOS;
		} else if (search.toLowerCase().contains("mint")) {
			distro = Distro.MINT;
		} else if (search.toLowerCase().contains("slackware")) {
			distro = Distro.SLACKWARE;
		} else if (search.toLowerCase().contains("arch")) {
			distro = Distro.ARCH;
		} else if (search.toLowerCase().contains("gentoo")) {
			distro = Distro.GENTOO;
		} else if (search.toLowerCase().contains("raspbian")) {
			distro = Distro.RASPBIAN;
		} else if (search.toLowerCase().contains("steam")) {
			distro = Distro.STEAMOS;
		} else {
			distro = Distro.UNKNOWN;
		}
		
		return distro;
	}

	/**
	 * Gets basic operating system string
	 * @return Few examples are "Windows 8.1", "Ubuntu Linux" and "Mac OS X Yosemite"
	 */
	public static String getShortOperatingSystem() {
		if (shortName == null) {
			if (OperatingSystem.getOperatingSystem() == OperatingSystem.LINUX) {
				try {
					String uname = getUname();
					
					if (uname != null) {
						if (uname.toLowerCase().contains("raspbian")) {
							return shortName = "Raspbian Linux";
						}
					}
					
					boolean lsb = true;
					
					File file = new File("/etc/lsb-release");
					 
					if (!file.exists()) {
						file = new File("/etc/os-release");
						lsb = false;
					}
					
					if (!file.exists()) {
						File[] files = new File("/etc/").listFiles();
						
						if (files != null) {
							for (File possible : files) {
								if (possible.getName().toLowerCase().endsWith("-release")) {
									file = possible;
									break;
								}
							}
						}
						lsb = false;
					}
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					String firstLine = null;
					String s;
					while ((s = reader.readLine()) != null) {
						if (firstLine == null) {
							firstLine = s;
						}
						if (lsb && s.startsWith("DISTRIB_ID=")) {
							shortName = s.substring(11, s.length()).replace("\"", "");
							if (!shortName.toLowerCase().contains("linux")) {
								shortName += " Linux";
							}
							reader.close();
							break;
						} else if (s.startsWith("NAME=")) {
							shortName = s.substring(5, s.length()).replace("\"", "");
							if (!shortName.toLowerCase().contains("linux")) {
								shortName += " Linux";
							}
							reader.close();
							break;
						}
					}
					reader.close();
					
					if (shortName == null) {
						shortName = firstLine;
						if (!shortName.toLowerCase().contains("linux")) {
							shortName += " Linux";
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					shortName = System.getProperty("os.name");
				}
			} else if (OperatingSystem.getOperatingSystem() == OperatingSystem.OSX) {
				shortName = System.getProperty("os.name") + " " + System.getProperty("os.version");
			} else if (OperatingSystem.getOperatingSystem() == OperatingSystem.SOLARIS) {
				shortName = "Solaris"; // I prefer Solaris over SunOS
			} else {
				shortName = System.getProperty("os.name");
			}
		}

		return shortName;
	}

	/**
	 * Gets long operating system string
	 * @return Will return "uname -a" on Linux, on other systems os.name + os.version + os.arch
	 */
	public static String getLongOperatingSystem() {
		if (longName == null) {
			if (OperatingSystem.getOperatingSystem() == OperatingSystem.LINUX) {
				longName = getUname();
				
				if (longName == null) {
					longName = System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + OperatingSystem.getArch(System.getProperty("os.arch"));		
				}
			} else {
				longName = System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + OperatingSystem.getArch(System.getProperty("os.arch"));
			}
		}

		return longName;
	}

	/**
	 * Gets arch, either 64-bit or x86
	 * @param arch
	 * @return
	 */
	public static Arch getArch(String s) {
		Arch arch;
		
		if (s.equalsIgnoreCase("x86") || s.equalsIgnoreCase("i386") || s.equalsIgnoreCase("i486") || s.equalsIgnoreCase("i586") || s.equalsIgnoreCase("i686")) {
			arch = Arch.x86;
		} else if (s.equalsIgnoreCase("x86_64") || s.equalsIgnoreCase("amd64") || s.equalsIgnoreCase("k8")) {
			arch = Arch.x86_64;
		} else {
			arch = Arch.UNKNOWN;
		}
		
		return arch;
	}
	
	/**
	 * Runs the command "uname -a" and reads the first line
	 * @return
	 */
	public static String getUname() {
		String uname = null;
		
		try {
			Process p = Runtime.getRuntime().exec(new String[] { "uname", "-a"});
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			longName = reader.readLine();
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return uname;
	}

}

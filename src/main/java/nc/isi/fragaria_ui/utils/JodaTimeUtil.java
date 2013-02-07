package nc.isi.fragaria_ui.utils;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeUtil {
	public static final DateTimeFormatter localDateFormatter = DateTimeFormat
			.forPattern("YYYYMMDD");

	// ///////////////////////////////////////////
	// From JodaTime types to standard Java types
	// ///////////////////////////////////////////

	public static java.sql.Date toSQLDate(DateMidnight dm) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.sql.Date d = (dm == null ? null
				: new java.sql.Date(dm.getMillis()));
		return d;
	}

	public static java.util.Date toJavaDate(DateMidnight dm) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.util.Date d = (dm == null ? null : new java.util.Date(
				dm.getMillis()));
		return d;
	}

	public static String toTimeZoneID(DateMidnight dm) {
		String s = (dm == null ? null : dm.getZone().getID());
		return s;
	}

	public static java.sql.Timestamp toSQLTimestamp(DateTime dt) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.sql.Timestamp ts = (dt == null ? null : new java.sql.Timestamp(
				dt.getMillis()));
		return ts;
	}

	public static java.util.Date toJavaDate(DateTime dt) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.util.Date ts = (dt == null ? null : new java.util.Date(
				dt.getMillis()));
		return ts;
	}

	public static String toTimeZoneID(DateTime dt) {
		String s = (dt == null ? null : dt.getZone().getID());
		return s;
	}

	public static java.sql.Date toSQLDate(LocalDate ld) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.sql.Date d = (ld == null ? null : new java.sql.Date(ld
				.toDateTimeAtStartOfDay().getMillis()));
		return d;
	}

	public static java.util.Date toJavaDate(LocalDate ld) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.util.Date d = (ld == null ? null : new java.util.Date(ld
				.toDateTimeAtStartOfDay().getMillis()));
		return d;
	}

	public static String toString(LocalDate ld) {
		// TODO - confirm this conversion always works, esp. across timezones
		String s = (ld == null ? null : localDateFormatter.withZone(
				DateTimeZone.UTC).print(ld));
		return s;
	}

	public static java.sql.Timestamp toSQLTimestamp(LocalDateTime ldt) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.sql.Timestamp ts = (ldt == null ? null : new java.sql.Timestamp(
				ldt.toDateTime().getMillis()));
		return ts;
	}

	public static java.util.Date toJavaDate(LocalDateTime ldt) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.util.Date d = (ldt == null ? null : new java.util.Date(ldt
				.toDateTime().getMillis()));
		return d;
	}

	public static java.sql.Time toSQLTime(LocalTime lt) {
		// TODO - confirm this conversion always works, esp. across timezones
		java.sql.Time t = (lt == null ? null : new java.sql.Time(
				lt.getMillisOfDay()));
		return t;
	}

	public static Integer toIntegerMillis(LocalTime lt) {
		Integer i = (lt == null ? null : new Integer(lt.getMillisOfDay()));
		return i;
	}

	public static String toString(LocalTime lt) {
		String s = (lt == null ? null : lt.toString());
		return s;
	}

	// ///////////////////////////////////////////
	// From standard Java types to JodaTime types
	// ///////////////////////////////////////////

	public static DateMidnight toDateMidnight(java.sql.Date d) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateMidnight dm = (d == null ? null : new DateMidnight(d));
		return dm;
	}

	public static DateMidnight toDateMidnight(java.util.Date d) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateMidnight dm = (d == null ? null : new DateMidnight(d));
		return dm;
	}

	public static DateMidnight toDateMidnight(java.sql.Date d, String timeZoneID) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateMidnight dm = (d == null ? null : new DateMidnight(d,
				DateTimeZone.forID(timeZoneID)));
		return dm;
	}

	public static DateMidnight toDateMidnight(java.util.Date d,
			String timeZoneID) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateMidnight dm = (d == null ? null : new DateMidnight(d,
				DateTimeZone.forID(timeZoneID)));
		return dm;
	}

	public static DateTime toDateTime(java.sql.Timestamp ts) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateTime dt = (ts == null ? null : new DateTime(ts));
		return dt;
	}

	public static DateTime toDateTime(java.util.Date d) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateTime dt = (d == null ? null : new DateTime(d));
		return dt;
	}

	public static DateTime toDateTime(java.sql.Timestamp ts, String timeZoneID) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateTime dt = (ts == null ? null : new DateTime(ts,
				DateTimeZone.forID(timeZoneID)));
		return dt;
	}

	public static DateTime toDateTime(java.util.Date d, String timeZoneID) {
		// TODO - confirm this conversion always works, esp. across timezones
		DateTime dt = (d == null ? null : new DateTime(d,
				DateTimeZone.forID(timeZoneID)));
		return dt;
	}

	public static LocalDate toLocalDate(java.sql.Date d) {
		// TODO - confirm this conversion always works, esp. across timezones
		LocalDate ld = (d == null ? null : LocalDate.fromDateFields(d));
		return ld;
	}

	public static LocalDate toLocalDate(java.util.Date d) {
		// TODO - confirm this conversion always works, esp. across timezones
		LocalDate ld = (d == null ? null : LocalDate.fromDateFields(d));
		return ld;
	}

	public static LocalDate toLocalDate(String s) {
		// TODO - confirm this conversion always works, esp. across timezones
		LocalDate ld = (s == null ? null : localDateFormatter
				.withZone(DateTimeZone.UTC).parseDateTime(s).toLocalDate());
		return ld;
	}

	public static LocalDateTime toLocalDateTime(java.sql.Timestamp ts) {
		// TODO - confirm this conversion always works, esp. across timezones
		LocalDateTime ldt = (ts == null ? null : LocalDateTime
				.fromDateFields(ts));
		return ldt;
	}

	public static LocalDateTime toLocalDateTime(java.util.Date d) {
		// TODO - confirm this conversion always works, esp. across timezones
		LocalDateTime ldt = (d == null ? null : LocalDateTime.fromDateFields(d));
		return ldt;
	}

	public static LocalTime toLocalTime(java.sql.Time t) {
		// TODO - confirm this conversion always works, esp. across timezones
		LocalTime lt = (t == null ? null : new LocalTime(t, DateTimeZone.UTC));
		return lt;
	}

	public static LocalTime toLocalTime(Integer i) {
		LocalTime lt = (i == null ? null : LocalTime.fromMillisOfDay(i));
		return lt;
	}

	public static LocalTime toLocalTime(String s) {
		LocalTime lt = (s == null ? null : new LocalTime(s));
		return lt;
	}
}

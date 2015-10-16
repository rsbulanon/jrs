package dynobjx.com.jrs.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import dynobjx.com.jrs.dao.Barangay;
import dynobjx.com.jrs.dao.BarangayDao;
import dynobjx.com.jrs.dao.Branch;
import dynobjx.com.jrs.dao.BranchDao;
import dynobjx.com.jrs.dao.BranchDetail;
import dynobjx.com.jrs.dao.BranchDetailDao;
import dynobjx.com.jrs.dao.City;
import dynobjx.com.jrs.dao.CityDao;
import dynobjx.com.jrs.dao.Country;
import dynobjx.com.jrs.dao.CountryDao;
import dynobjx.com.jrs.dao.DaoMaster;
import dynobjx.com.jrs.dao.DaoSession;
import dynobjx.com.jrs.dao.International;
import dynobjx.com.jrs.dao.Province;
import dynobjx.com.jrs.dao.ProvinceDao;
import dynobjx.com.jrs.dao.UserInfo;
import dynobjx.com.jrs.dao.UserInfoDao;


/**
 * Created by rsbulanon on 6/24/15.
 */
public class DaoHelper {

    private static SQLiteDatabase DB;
    private static BranchDao DAO_BRANCH;
    private static ProvinceDao DAO_PROVINCE;
    private static CityDao DAO_CITY;
    private static BarangayDao DAO_BRGY;
    private static CountryDao DAO_COUNTRY;
    private static UserInfoDao DAO_USER_INFO;
    private static BranchDetailDao DAO_BRANCH_DETAIL;

    public static void initialize(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "dev-jrs-db-v1.0", null);
        DB = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(DB);
        DaoSession daoSession = daoMaster.newSession();
        DAO_BRANCH = daoSession.getBranchDao();
        DAO_PROVINCE = daoSession.getProvinceDao();
        DAO_CITY = daoSession.getCityDao();
        DAO_BRGY = daoSession.getBarangayDao();
        DAO_COUNTRY = daoSession.getCountryDao();
        DAO_USER_INFO = daoSession.getUserInfoDao();
        DAO_BRANCH_DETAIL = daoSession.getBranchDetailDao();
    }

    /** managing of branch */
    public static void addBranch(Branch branch) { DAO_BRANCH.insert(branch); }

    public static List<Branch> getAllBranches() { return DAO_BRANCH.loadAll(); }

    public static long getBranchesCount() { return DAO_BRANCH.count(); }

    /** managing of province */
    public static void addProvince(Province province) { DAO_PROVINCE.insert(province); }

    public static List<Province> getAllProvinces() { return DAO_PROVINCE.loadAll(); }

    public static long getProvinceCount() { return DAO_PROVINCE.count(); }

    /** managing of city */
    public static void addCity(City city) { DAO_CITY.insert(city); }

    public static List<City> getCitiesByProvId(int provId) {
        return DAO_CITY.queryBuilder().where(CityDao.Properties.ProvinceId.eq(provId)).list();
    }

    /** managing of barangay */
    public static void addBarangay(Barangay barangay) { DAO_BRGY.insert(barangay); }

    public static List<Barangay> getBarangays(int provId, int muniId) {
        return DAO_BRGY.queryBuilder().where(BarangayDao.Properties.ProvinceId.eq(provId),
                BarangayDao.Properties.MunicipalityId.eq(muniId)).list();
    }

    public static CountryDao getDaoCountry() {
        return DAO_COUNTRY;
    }

    /**managing countries*/
    public static void addCountry(Country country) { DAO_COUNTRY.insert(country); }

    public static List<Country> getAllCountries() { return DAO_COUNTRY.loadAll(); }

    public static long getCountryCount() { return DAO_PROVINCE.count(); }

    /** managing of user info */
    public static void addUserInfo(UserInfo userInfo) { DAO_USER_INFO.insert(userInfo); }

    /** get user info */
    public static UserInfo getUserInfo() {
        return DAO_USER_INFO.loadByRowId(1);
    }

    /** update user info */
    public static void updateUserInfo(UserInfo userInfo) {
        DAO_USER_INFO.update(userInfo);
    }

    /** managing of branch detail */
    public static void addNewBranchDetails(BranchDetail branchDetail) { DAO_BRANCH_DETAIL.insert(branchDetail); }

    /** get branch details by branch id*/
    public static BranchDetail getBranchDetailByBranchId(int branchId) {
        return DAO_BRANCH_DETAIL.queryBuilder().where(BranchDetailDao.Properties.BranchId.eq(branchId)).unique();
    }

    /** get all barangay */
    public static List<Barangay> getAllBarangays() {
        return DAO_BRGY.loadAll();
    }

    public static void clearUserInfo() {
        DAO_USER_INFO.deleteAll();
    }

}

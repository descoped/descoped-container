package io.descoped.hystrix.commands.geolocation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocation implements Serializable {

    private static final long serialVersionUID = -4721933434073766012L;

    @JsonProperty("geoplugin_request")
    private String ipAddress;
    
    @JsonProperty("geoplugin_city")
    private String geoplugin_city;

    @JsonProperty("geoplugin_region")
    private String geoplugin_region;
    
    @JsonProperty("geoplugin_areaCode")
    private String geoplugin_areaCode;
    
    @JsonProperty("geoplugin_dmaCode")
    private String geoplugin_dmaCode;
    
    @JsonProperty("geoplugin_countryCode")
    private String geoplugin_countryCode;
    
    @JsonProperty("geoplugin_countryName")
    private String geoplugin_countryName;
    
    @JsonProperty("geoplugin_continentCode")
    private String geoplugin_continentCode;
    
    @JsonProperty("geoplugin_latitude")
    private String geoplugin_latitude;
    
    @JsonProperty("geoplugin_longitude")
    private String geoplugin_longitude;
    
    @JsonProperty("geoplugin_regionCode")
    private String geoplugin_regionCode;
    
    @JsonProperty("geoplugin_regionName")
    private String geoplugin_regionName;
    
    @JsonProperty("geoplugin_currencyCode")
    private String geoplugin_currencyCode;
    
    @JsonProperty("geoplugin_currencySymbol")
    private String geoplugin_currencySymbol;
    
    @JsonProperty("geoplugin_currencyConverter")
    private String geoplugin_currencyConverter;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCity() {
        return geoplugin_city;
    }

    public void setCity(String city) {
        this.geoplugin_city = city;
    }

    public String getRegion() {
        return geoplugin_region;
    }

    public void setRegion(String region) {
        this.geoplugin_region = region;
    }

    public String getAreaCode() {
        return geoplugin_areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.geoplugin_areaCode = areaCode;
    }

    public String getDmaCode() {
        return geoplugin_dmaCode;
    }

    public void setDmaCode(String dmaCode) {
        this.geoplugin_dmaCode = dmaCode;
    }

    public String getCountryCode() {
        return geoplugin_countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.geoplugin_countryCode = countryCode;
    }

    public String getCountryName() {
        return geoplugin_countryName;
    }

    public void setcCuntryName(String countryName) {
        this.geoplugin_countryName = countryName;
    }

    public String getContinentCode() {
        return geoplugin_continentCode;
    }

    public void setContinentCode(String continentCode) {
        this.geoplugin_continentCode = continentCode;
    }

    public String getLatitude() {
        return geoplugin_latitude;
    }

    public void setLatitude(String latitude) {
        this.geoplugin_latitude = latitude;
    }

    public String getLongitude() {
        return geoplugin_longitude;
    }

    public void setLongitude(String longitude) {
        this.geoplugin_longitude = longitude;
    }

    public String getRegionCode() {
        return geoplugin_regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.geoplugin_regionCode = regionCode;
    }

    public String getRegionName() {
        return geoplugin_regionName;
    }

    public void setRegionName(String regionName) {
        this.geoplugin_regionName = regionName;
    }

    public String getCurrencyCode() {
        return geoplugin_currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.geoplugin_currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return geoplugin_currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.geoplugin_currencySymbol = currencySymbol;
    }

    public String getCurrencyConverter() {
        return geoplugin_currencyConverter;
    }

    public void setCurrencyConverter(String currencyConverter) {
        this.geoplugin_currencyConverter = currencyConverter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geoplugin_areaCode == null) ? 0 : geoplugin_areaCode.hashCode());
        result = prime * result + ((geoplugin_city == null) ? 0 : geoplugin_city.hashCode());
        result = prime * result + ((geoplugin_continentCode == null) ? 0 : geoplugin_continentCode.hashCode());
        result = prime * result + ((geoplugin_countryCode == null) ? 0 : geoplugin_countryCode.hashCode());
        result = prime * result + ((geoplugin_countryName == null) ? 0 : geoplugin_countryName.hashCode());
        result = prime * result + ((geoplugin_currencyCode == null) ? 0 : geoplugin_currencyCode.hashCode());
        result = prime * result + ((geoplugin_currencyConverter == null) ? 0 : geoplugin_currencyConverter.hashCode());
        result = prime * result + ((geoplugin_currencySymbol == null) ? 0 : geoplugin_currencySymbol.hashCode());
        result = prime * result + ((geoplugin_dmaCode == null) ? 0 : geoplugin_dmaCode.hashCode());
        result = prime * result + ((geoplugin_latitude == null) ? 0 : geoplugin_latitude.hashCode());
        result = prime * result + ((geoplugin_longitude == null) ? 0 : geoplugin_longitude.hashCode());
        result = prime * result + ((geoplugin_region == null) ? 0 : geoplugin_region.hashCode());
        result = prime * result + ((geoplugin_regionCode == null) ? 0 : geoplugin_regionCode.hashCode());
        result = prime * result + ((geoplugin_regionName == null) ? 0 : geoplugin_regionName.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "ipAddress='" + ipAddress + '\'' +
                ", geoplugin_city='" + geoplugin_city + '\'' +
                ", geoplugin_region='" + geoplugin_region + '\'' +
                ", geoplugin_areaCode='" + geoplugin_areaCode + '\'' +
                ", geoplugin_dmaCode='" + geoplugin_dmaCode + '\'' +
                ", geoplugin_countryCode='" + geoplugin_countryCode + '\'' +
                ", geoplugin_countryName='" + geoplugin_countryName + '\'' +
                ", geoplugin_continentCode='" + geoplugin_continentCode + '\'' +
                ", geoplugin_latitude='" + geoplugin_latitude + '\'' +
                ", geoplugin_longitude='" + geoplugin_longitude + '\'' +
                ", geoplugin_regionCode='" + geoplugin_regionCode + '\'' +
                ", geoplugin_regionName='" + geoplugin_regionName + '\'' +
                ", geoplugin_currencyCode='" + geoplugin_currencyCode + '\'' +
                ", geoplugin_currencySymbol='" + geoplugin_currencySymbol + '\'' +
                ", geoplugin_currencyConverter='" + geoplugin_currencyConverter + '\'' +
                '}';
    }
}

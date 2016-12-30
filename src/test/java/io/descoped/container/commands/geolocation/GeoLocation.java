package io.descoped.container.commands.geolocation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocation implements Serializable {

    private static final long serialVersionUID = -4721933434073766012L;

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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeoLocation other = (GeoLocation) obj;
        if (geoplugin_areaCode == null) {
            if (other.geoplugin_areaCode != null)
                return false;
        } else if (!geoplugin_areaCode.equals(other.geoplugin_areaCode))
            return false;
        if (geoplugin_city == null) {
            if (other.geoplugin_city != null)
                return false;
        } else if (!geoplugin_city.equals(other.geoplugin_city))
            return false;
        if (geoplugin_continentCode == null) {
            if (other.geoplugin_continentCode != null)
                return false;
        } else if (!geoplugin_continentCode.equals(other.geoplugin_continentCode))
            return false;
        if (geoplugin_countryCode == null) {
            if (other.geoplugin_countryCode != null)
                return false;
        } else if (!geoplugin_countryCode.equals(other.geoplugin_countryCode))
            return false;
        if (geoplugin_countryName == null) {
            if (other.geoplugin_countryName != null)
                return false;
        } else if (!geoplugin_countryName.equals(other.geoplugin_countryName))
            return false;
        if (geoplugin_currencyCode == null) {
            if (other.geoplugin_currencyCode != null)
                return false;
        } else if (!geoplugin_currencyCode.equals(other.geoplugin_currencyCode))
            return false;
        if (geoplugin_currencyConverter == null) {
            if (other.geoplugin_currencyConverter != null)
                return false;
        } else if (!geoplugin_currencyConverter.equals(other.geoplugin_currencyConverter))
            return false;
        if (geoplugin_currencySymbol == null) {
            if (other.geoplugin_currencySymbol != null)
                return false;
        } else if (!geoplugin_currencySymbol.equals(other.geoplugin_currencySymbol))
            return false;
        if (geoplugin_dmaCode == null) {
            if (other.geoplugin_dmaCode != null)
                return false;
        } else if (!geoplugin_dmaCode.equals(other.geoplugin_dmaCode))
            return false;
        if (geoplugin_latitude == null) {
            if (other.geoplugin_latitude != null)
                return false;
        } else if (!geoplugin_latitude.equals(other.geoplugin_latitude))
            return false;
        if (geoplugin_longitude == null) {
            if (other.geoplugin_longitude != null)
                return false;
        } else if (!geoplugin_longitude.equals(other.geoplugin_longitude))
            return false;
        if (geoplugin_region == null) {
            if (other.geoplugin_region != null)
                return false;
        } else if (!geoplugin_region.equals(other.geoplugin_region))
            return false;
        if (geoplugin_regionCode == null) {
            if (other.geoplugin_regionCode != null)
                return false;
        } else if (!geoplugin_regionCode.equals(other.geoplugin_regionCode))
            return false;
        if (geoplugin_regionName == null) {
            if (other.geoplugin_regionName != null)
                return false;
        } else if (!geoplugin_regionName.equals(other.geoplugin_regionName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GeoLocation [city=" + geoplugin_city + ", region=" + geoplugin_region
                + ", areaCode=" + geoplugin_areaCode + ", dmaCode=" + geoplugin_dmaCode
                + ", countryCode=" + geoplugin_countryCode + ", countryName=" + geoplugin_countryName
                + ", continentCode=" + geoplugin_continentCode + ", latitude=" + geoplugin_latitude
                + ", longitude=" + geoplugin_longitude + ", regionCode=" + geoplugin_regionCode
                + ", regionName=" + geoplugin_regionName + ", currencyCode=" + geoplugin_currencyCode
                + ", currencySymbol=" + geoplugin_currencySymbol + ", currencyConverter="
                + geoplugin_currencyConverter + "]";
    }
    
}

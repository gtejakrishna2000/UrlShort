package com.example.UrlShort.Service;
import com.example.UrlShort.Dtos.DashboardDto;
import com.example.UrlShort.Dtos.UrlDto;
import com.example.UrlShort.Models.Customer;
import com.example.UrlShort.Models.Url;
import com.example.UrlShort.Repository.CustomerRepository;
import com.example.UrlShort.Repository.UrlRepository;
import com.google.common.hash.Hashing;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlService {

private static final Logger logger = LoggerFactory.getLogger(UrlService.class);
    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CustomerRepository customerRepository;


    public Url generateShortLink(UrlDto urlDto, String userName) {

        Customer customer = (Customer) customerRepository.findByUsername(userName);

        if(StringUtils.isNotEmpty(urlDto.getUrl()))
        {
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
            Url urlToRet = persistShortLink(urlToPersist,customer);

            if(urlToRet != null)
                return urlToRet;

            return null;
        }
        return null;
    }

    public Url generateDashboard(UrlDto urlDto, String userName) {

        Customer customer = (Customer) customerRepository.findByUsername(userName);

        if(StringUtils.isNotEmpty(urlDto.getUrl()))
        {
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
            Url urlToRet = persistShortLink(urlToPersist,customer);

            if(urlToRet != null)
                return urlToRet;

            return null;
        }
        return null;
    }
    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate)
    {
        if(StringUtils.isBlank(expirationDate))
        {
            return creationDate.plusSeconds(600);
        }
        LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        return expirationDateToRet;
    }

    public DashboardDto dashboard(Customer customer){

        DashboardDto dashboardDto = new DashboardDto();
        List<Url> list = customer.getUrl();

        for(Url url : list)
        {
            dashboardDto.getUrlClickMap().put(url.getShortLink(),url.getClicks());
        }
        return dashboardDto;
    }

    private String encodeUrl(String url)
    {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return  encodedUrl;
    }

    public Url persistShortLink(Url url,Customer customer) {
        List<Url> urlList = customer.getUrl();
        urlList.add(url);
        customer.setUrl(urlList);
        url.setCustomer(customer);
        customerRepository.save(customer);
        Url urlToRet = urlRepository.save(url);
        return urlToRet;
    }

    public Url getEncodedUrl(String url) {
        Url urlToRet = urlRepository.findByShortLink(url);
        return urlToRet;
    }
    public void updateClicks(Url url){
        urlRepository.save(url);
    }
    public void deleteShortLink(Url url) {

        urlRepository.delete(url);
    }
}

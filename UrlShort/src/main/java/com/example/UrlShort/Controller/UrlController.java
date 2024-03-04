package com.example.UrlShort.Controller;
import java.io.IOException;
import java.time.LocalDateTime;
import com.example.UrlShort.Repository.CustomerRepository;
import com.example.UrlShort.Dtos.*;
import com.example.UrlShort.Models.Customer;
import com.example.UrlShort.Models.Url;
import com.example.UrlShort.Service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/urls")
public class UrlController {
@Autowired
private UrlService urlService;
@Autowired
private CustomerRepository customerRepository;

    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            userName = userDetails.getUsername();

            System.out.println("user id " + userName);
        }
        Url urlToRet = urlService.generateShortLink(urlDto, userName);

        if (urlToRet != null) {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            urlResponseDto.setShortLink(urlToRet.getShortLink());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("There was an error processing your request. please try again.");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);

    }

    @GetMapping("/{shortLink}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> redirectToOriginalUrl1(@PathVariable String shortLink, HttpServletResponse response) throws IOException {

        if(StringUtils.isEmpty(shortLink))
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Invalid Url");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        Url urlToRet = urlService.getEncodedUrl(shortLink);

        if(urlToRet == null)
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url does not exist or it might have expired!");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now()))
        {
            urlService.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url Expired. Please try generating a fresh one.");
            urlErrorResponseDto.setStatus("200");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        urlToRet.setClicks(urlToRet.getClicks()+1);
        urlService.updateClicks(urlToRet);
        return new ResponseEntity<>(urlToRet.getOriginalUrl(), HttpStatus.OK);
    }
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<DashboardDto> generateDashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            userName = userDetails.getUsername();

        }
        Customer customer = (Customer)customerRepository.findByUsername(userName);
        DashboardDto dashboardDto = urlService.dashboard(customer);
        return new ResponseEntity<DashboardDto>(dashboardDto, HttpStatus.OK);

    }
}
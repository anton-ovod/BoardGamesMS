package org.example.modules.tcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.FileContentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class TcpController {
    private final TcpClientService tcpClientService;

    public TcpController(TcpClientService tcpClientService) {
        this.tcpClientService = tcpClientService;
    }

    @GetMapping
    public ResponseEntity<FileContentResponse> GetFile(@RequestParam("name") String name)
    {
        if(!name.toLowerCase().endsWith(".txt") && !name.toLowerCase().endsWith(".json"))
        {
            return ResponseEntity.badRequest().build();
        }

        try{
            byte[] data = tcpClientService.requestFile(name);

            String text = new String(data, StandardCharsets.UTF_8);

            if(name.toLowerCase().endsWith(".json"))
            {
                ObjectMapper objectMapper = new ObjectMapper();
                Object json = objectMapper.readValue(text, Object.class);
                return ResponseEntity.ok(new FileContentResponse(name, json.toString()));
            }

            return ResponseEntity.ok(new FileContentResponse(name, text));
        }
        catch (FileNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }
}

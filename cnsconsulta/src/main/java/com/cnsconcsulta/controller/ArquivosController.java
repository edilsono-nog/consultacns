package com.cnsconcsulta.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnsconcsulta.model.Municipios;
import com.cnsconcsulta.repository.MunicipiosRepository;

@RestController
@RequestMapping(value = "/excel")
public class ArquivosController {
	
	@Autowired
	private MunicipiosRepository municipiosRepository;
	
	
	@SuppressWarnings("resource")
	@PostMapping(value = "municipios")
	private String arquivos() throws IOException {
		
		String csvArquivo = "D:\\municipios.csv";
		
		BufferedReader conteudoCSV = null;
		
		String linha = "";
		
		String csvSeparadorCampo = ";";
		
		Municipios municipios = new Municipios();
		
		try {
			
			conteudoCSV = new BufferedReader(new FileReader(csvArquivo));
			
			while ((linha = conteudoCSV.readLine()) != null) {
				
				String [] municipio = linha.split(csvSeparadorCampo);
				
				municipios.setCodigo(municipio[0]);
				municipios.setNomeMunicipio(municipio[1]);
				
				municipiosRepository.save(municipios);
				municipios = new Municipios();
				
				System.out.println("[Codigo : " + municipio[0] + " Municipio : " + municipio[1]+"]");
			}
		} catch (FileNotFoundException e){
			System.out.println("Arquivo não encontrado: \n"+e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBounds; \n"+e.getMessage());
		} catch (IOException e) {
			System.out.println("IO erro: \n"+e.getMessage());
		} finally {
			if (conteudoCSV != null) {
				try {
					conteudoCSV.close();
				} catch (IOException e) {
					System.out.println("IO Erro: \n"+e.getMessage());
				}
			}
		}
		
		
		return csvArquivo;
	}

}

package com.uniovi.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uniovi.entities.Post;
import com.uniovi.repositories.PostRepository;
import com.uniovi.services.util.CreateFolder;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

	public void add(Post post) {
		postRepository.save(post);
	}

	public Page<Post> findAll(Pageable pageable, Long id) {
		return postRepository.findAllByUserId(pageable, id);
	}

	/**
	 * Guarda una imagen en la aplicación
	 * 
	 * @param img
	 * @return
	 * @throws IOException
	 */
	public String saveImg(MultipartFile img) throws IOException {
		saveImgResource(img);
		saveImgTarget(img);
		return img.getOriginalFilename();
	}

	/**
	 * Guarda una imagen en un determinado directorio
	 * 
	 * @param img
	 * @param path
	 * @throws IOException
	 */
	private void saveImgByPath(MultipartFile img, String path)
			throws IOException {
		String fileName = img.getOriginalFilename();
		InputStream is = img.getInputStream();
		Files.copy(is, Paths.get(path + fileName),
				StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Guarda en una imagen en la carpeta target, para que así se visualice sin
	 * recargar la aplicación. Pero es transient, no se guarda cuando se acaba
	 * la ejecución.
	 * 
	 * @param img
	 * @throws IOException
	 */
	private void saveImgTarget(MultipartFile img) throws IOException {
		CreateFolder.createFolder("target/classes/static/imgUser/");
		saveImgByPath(img, "target/classes/static/imgUser/");
	}

	/**
	 * Guarda en una imagen en la carpeta resource, aquí al guardarla, no se ve
	 * hasta que no se recarga la aplicación pero si es persistente. No se borra
	 * cuando se reinicia la aplicación
	 * 
	 * @param img
	 * @throws IOException
	 */
	private void saveImgResource(MultipartFile img) throws IOException {
		CreateFolder.createFolder("src/main/resources/static/imgUser");
		saveImgByPath(img, "src/main/resources/static/imgUser/");
	}

}
